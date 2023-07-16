#include "p3210150-p3210122-pizzeria.h"

//VARIABLES, MUTEXES AND CONDITION VARIABLES DECLARATION
//resources variables
uint cooks = N_cook;            //number of cooks
uint ovens = N_oven;            //number of ovens
uint packers = N_packer;        //number of packers
uint deliverers = N_deliverer;  //number of deliverers

//global counter and sum variables
uint profit;                //total profit
uint plain;                 //counter of plain pizzas sold
uint special;               //counter of special pizzas sold
uint succeeded;             //counter of succeeded orders
uint failed;                //counter of failed orders
uint happy;                 //counter of happy customers
uint disappointed;          //counter of disappointed customers

//global time variables
double total_waiting_time;
double total_cooling_time;
double max_waiting_time = -1;
double max_cooling_time = -1;
double min_waiting_time = BILLION;
double min_cooling_time = BILLION;

//mutexes for resources 
pthread_mutex_t cooks_count_mutex;          //locks cooks
pthread_mutex_t ovens_count_mutex;          //locks ovens
pthread_mutex_t packers_count_mutex;        //locks packers
pthread_mutex_t deliverers_count_mutex;     //locks deliverers

//mutexes for global variables
pthread_mutex_t count_and_profit_mutex;     //locks pizza count and profit       
pthread_mutex_t satisfied_mutex;            //locks happy and disappointed 
pthread_mutex_t check_payment_mutex;        //locks failed and succeeded
pthread_mutex_t lockscreen_mutex;           //locks output

//mutexes for time calculations
pthread_mutex_t total_waiting_time_mutex;
pthread_mutex_t total_cooling_time_mutex;
pthread_mutex_t max_waiting_time_mutex;
pthread_mutex_t max_cooling_time_mutex;
pthread_mutex_t min_waiting_time_mutex;
pthread_mutex_t min_cooling_time_mutex;

//condition variables for resources
pthread_cond_t cooks_cond;              //condition variable for cooks
pthread_cond_t ovens_cond;              //condition variable for ovens
pthread_cond_t packers_cond;            //condition variable for packers
pthread_cond_t deliverers_cond;         //condition variable for deliverers

uint N_cust;            //number of customers
uint seed;

//ROUTINE FUNCTION
void *order(void *order_id){
    Order *s_order = (Order *) order_id;
    //local seed
    uint loc_seed = seed * (s_order->id);

    struct timespec start_timespec;         //arrived
    struct timespec payment_timespec;       //approved or cancelled 
    struct timespec cooked_timespec;        //cooked
    struct timespec baked_timespec;         //baked
    struct timespec packed_timespec;        //packed
    struct timespec delivered_timespec;     //delivered
    
    //hold time when ordered arrived
    clock_get_time_check(&start_timespec);
    //every order is being examined for a random integer number in [1,3] 
    uint time_payment = rand_generator(&loc_seed, T_paymentlow, T_paymenthigh);
    
    sleep(time_payment);

    //random integer in [1,100] <= 10 then fail else success
    uint success = rand_generator(&loc_seed, 1, 100);
    s_order->is_canceled = (success <= P_fail);

    //hold time when order is examined -> approved or cancelled
    clock_get_time_check(&payment_timespec);
    s_order->payment_time = get_time_difference(&start_timespec, &payment_timespec);

    //print message safely -> order approved or cancelled and in how long
    mutex_lock(&lockscreen_mutex);
    printf("\nOrder #%d %s in approximately %.2f %s after being received.\n", s_order->id, s_order->is_canceled ? "cancelled" : "approved", round(s_order->payment_time), (round(s_order->payment_time) == 1) ? "minute" : "minutes");
    mutex_unlock(&lockscreen_mutex);

    //update safely failed or succeeded(if fail -> exit thread)
    mutex_lock(&check_payment_mutex);
    s_order->is_canceled ? (failed++, mutex_unlock(&check_payment_mutex), pthread_exit(&s_order->id)) : succeeded++;
    mutex_unlock(&check_payment_mutex);

    
    mutex_lock(&count_and_profit_mutex);
    //random amount of pizza in [1,5]
    s_order->pizza_quantity = rand_generator(&loc_seed, N_orderlow, N_orderhigh); 
    for (uint i = 0; i < s_order->pizza_quantity; i++) {
        //random integer in [1,100] <= 60 then plain else special
        uint pizzaType = rand_generator(&loc_seed, 1, 100);
        bool isPlain = (pizzaType <= P_plain);
        //update safely count of plain and special pizzas and profit
        plain += isPlain ? 1 : 0;
        special += isPlain ? 0 : 1;
        profit += isPlain ? C_plain : C_special;
    }
    mutex_unlock(&count_and_profit_mutex);

    //wait for available cook
    wait_for_resource(&cooks, 0, &cooks_count_mutex, &cooks_cond);

    //cook found,wait for each pizza of order to be cooked
    sleep(T_prep * s_order->pizza_quantity);

    //hold time when order is cooked
    clock_get_time_check(&cooked_timespec);
    s_order->cooking_time = get_time_difference(&payment_timespec, &cooked_timespec);

    //wait for available ovens(sum of ovens available >= quantity of order)
    wait_for_resource(&ovens, s_order->pizza_quantity, &ovens_count_mutex, &ovens_cond);

    //order in the oven -> release cook and signal asleep thread in cooking state
    signal_resource(&cooks, 0, &cooks_count_mutex, &cooks_cond);

    //wait for order to be baked
    sleep(T_bake);

    //hold time when order is baked
    clock_get_time_check(&baked_timespec);
    s_order->baking_time = get_time_difference(&cooked_timespec, &baked_timespec);

    //wait for available packer
    wait_for_resource(&packers, 0, &packers_count_mutex, &packers_cond);

    //packer found, wait for each pizza of order to be packed
    sleep(T_pack * s_order->pizza_quantity);

    //hold time when order is packed
    clock_get_time_check(&packed_timespec);
    s_order->packing_time = get_time_difference(&baked_timespec, &packed_timespec);
    s_order->packed_time = get_time_difference(&start_timespec, &packed_timespec);

    //print message safely -> order got ready(paid,cooked,baked,packed) and in how long
    mutex_lock(&lockscreen_mutex);
    printf("\nOrder #%d got ready in approximately %.2f minutes after being received.\n", s_order->id, round(s_order->packed_time));
    mutex_unlock(&lockscreen_mutex);

    //signal every thread in baking state,if we used signal -> threads that exceed the available ovens may not immediately execute and will go back to sleep
    mutex_lock(&ovens_count_mutex);
    ovens += s_order->pizza_quantity;
    pthread_cond_broadcast(&ovens_cond);
    mutex_unlock(&ovens_count_mutex);

    //wait for available deliverer
    wait_for_resource(&deliverers, 0, &deliverers_count_mutex, &deliverers_cond);

    //deliverer found,release packer and signal asleep thread in packing state
    signal_resource(&packers, 0, &packers_count_mutex, &packers_cond);

    //random one-way time in [5,15]
    uint oneway_time = rand_generator(&loc_seed, T_dellow, T_delhigh); 

    //wait for delivery
    sleep(oneway_time); 

    //hold time when order is delivered
    clock_get_time_check(&delivered_timespec);
    s_order->delivery_time = get_time_difference(&packed_timespec, &delivered_timespec);

    //calculate cooling(from out of the oven until delivered)
    s_order->cooling_time = get_time_difference(&baked_timespec, &delivered_timespec);

    //update safely total cooling time
    mutex_lock(&total_cooling_time_mutex);
    total_cooling_time += s_order->cooling_time;
    mutex_unlock(&total_cooling_time_mutex);

    //EXTRA : update safely happy and disappointed customers according to cooling time
    mutex_lock(&satisfied_mutex);
    s_order->cooling_time >= 15 ?  disappointed++ : happy++;
    mutex_unlock(&satisfied_mutex);

    //update safely maximum cooling time
    update_max_time(s_order->cooling_time, &max_cooling_time_mutex, &max_cooling_time);
    //EXTRA : update safely minimum cooling time 
    update_min_time(s_order->cooling_time, &min_cooling_time_mutex, &min_cooling_time);

    //calculate waiting time(cooling time plus payment,cook,bake)
    s_order->waiting_time = s_order->cooling_time + get_time_difference(&start_timespec, &baked_timespec);

    //update safely total waiting time
    mutex_lock(&total_waiting_time_mutex);
    total_waiting_time += s_order->waiting_time;
    mutex_unlock(&total_waiting_time_mutex);
    
    //update safely maximum waiting time
    update_max_time(s_order->waiting_time, &max_waiting_time_mutex, &max_waiting_time);
    //EXTRA : update safely minimum waiting time 
    update_min_time(s_order->waiting_time, &min_waiting_time_mutex, &min_waiting_time);

    //print message safely -> order delivered and in how long
    mutex_lock(&lockscreen_mutex);
    printf("\nOrder #%d delivered in approximately %.2f minutes after being received.\n", s_order->id, round(s_order->waiting_time));
    mutex_unlock(&lockscreen_mutex);
    
    //wait deliverer to return
    sleep(oneway_time);

    //deliverer is back,release deliverer and signal asleep thread in delivering state
    signal_resource(&deliverers, 0, &deliverers_count_mutex, &deliverers_cond);

    //exit order
    pthread_exit(&s_order->id);
}

//MAIN FUNCTION
int main(int argc, char* argv[]){
    //check number of arguments
    if(argc != 3){
        perror("Error: Invalid number of arguments.\n");
        exit(-1);
    }

    //extract first argument -> Number of customers
    N_cust = atoi(argv[1]);

    //validate number of customers
    if(N_cust < 1){
        perror("Error: Invalid number of customers.\n");
        exit(-1);
    }

    //extract second argument -> seed
    seed = atoi(argv[2]);

    //welcoming message
    printf("WELCOME TO OUR ONLINE PIZZA STORE! RUSH TO BECOME 1 OUT OF 100 TO BE SERVED.\n");

    //allocate memory for threads
    pthread_t *threads;
    threads = malloc(N_cust * sizeof(pthread_t));
    if(threads == NULL){
        perror("Error: Failed to allocate memory.\n");
        return -1;
    }

    //allocate memory for orders
    Order *orders;
    orders = malloc(N_cust * sizeof(Order));
    if(orders == NULL){
        perror("Error: Failed to allocate memory.\n");
        //deallocate memory
        free(threads);
        return -1;
    }

    //initialize mutexes and condition variables safely
    mutex_init();
    cond_init();

    //create 100 threads(orders)
    for(int i = 0; i < N_cust; i++){
        uint loc_seed = seed * (i + 1);
        orders[i].id = i + 1;
    	if(pthread_create(&threads[i], NULL, order, (void*) &orders[i]) != 0){
            printf("Error: Failed to create order #%d.\n", i + 1);
            //deallocate memory
            free(threads);
            free(orders);
            //destroy mutexes and condition variables safely
            mutex_destroy();
            cond_destroy();
            exit(-1);
        }
        //every order arrives after a random integer number in [1,3]
        sleep(rand_generator(&loc_seed, T_orderlow, T_orderhigh)); 
    }

    //destroy threads
    for(int i = 0; i < N_cust; i++){
        if(pthread_join(threads[i], NULL) != 0){
            printf("Error: Failed to join order #%d.\n", i + 1);
            //deallocate memory
            free(threads);
            free(orders);
            //destroy mutexes and condition variables safely
            mutex_destroy();
            cond_destroy();
            exit(-1);
        }
    }

    //EXTRA: Full stats of every order(id,paid or not,exact time needed for every state)
    /*for (int i = 0; i < N_cust; i++) {
        print_order(orders[i]);
    } OUTPUT IN REPORT */

    //Calculate average times
    double avg_waiting_time = total_waiting_time / (double) succeeded;
    double avg_cooling_time = total_cooling_time / (double) succeeded;

    //Print Statistics
    printf("\n");
    printf("\nSTATISTICS OF THE DAY:\n");
    printf("PROFIT: %d €\n", profit);
    printf("TYPE OF PIZZAS SOLD: %d plain and %d special\n", plain, special);
    printf("SUCCESSFUL ORDERS: %d\n", succeeded);
    printf("FAILED ORDERS: %d\n", failed);
    printf("HAPPY CUSTOMERS: %d\n", happy);
    printf("DISAPPOINTED CUSTOMERS: %d\n", disappointed);
    printf("MAXIMUM WAITING TIME: %.2f minutes\n", round(max_waiting_time));
    printf("MINIMUM WAITING TIME: %.2f minutes\n", round(min_waiting_time));
    printf("AVERAGE WAITING TIME: %.2f minutes\n", round(avg_waiting_time));
    printf("MAXIMUM COOLING TIME: %.2f minutes\n", round(max_cooling_time));
    printf("MINIMUM COOLING TIME: %.2f minutes\n", round(min_cooling_time));
    printf("AVERAGE COOLING TIME: %.2f minutes\n", round(avg_cooling_time));

    //Destroy mutexes and condition variables safely
    mutex_destroy();
    cond_destroy();

    //Deallocate memory
    free(threads);
    free(orders);

    return 1;
}

//FUNCTION IMPLEMENTATION
//Helper Functions
double get_time_difference(struct timespec *start_time, struct timespec *stop_time){
    //Calculate difference in seconds
    double dif_sec = stop_time->tv_sec - start_time->tv_sec;
    //Calculate difference in nanoseconds
    double dif_nsec = (double)(stop_time->tv_nsec - start_time->tv_nsec) / BILLION;
    //Exact difference
    return dif_sec + dif_nsec;
}

uint rand_generator(uint *seed, uint min, uint max){
    //Adjust random number in [min,max]
    return (rand_r(seed) % (max - min + 1) + min);
}

void update_max_time(double new_time, pthread_mutex_t *max_mutex, double *cur_max){
    mutex_lock(max_mutex);
    //Update maximum time safely
    if(new_time > *cur_max){
        *cur_max = new_time;
    }
    mutex_unlock(max_mutex);
}

void update_min_time(double new_time, pthread_mutex_t *min_mutex, double *cur_min){
    mutex_lock(min_mutex);
    //Update minimum time safely
    if(new_time < *cur_min){
        *cur_min = new_time;
    }
    mutex_unlock(min_mutex);
}

void wait_for_resource(uint *resource, uint value, pthread_mutex_t *resource_mutex, pthread_cond_t *resource_cond){
    mutex_lock(resource_mutex);
    //Waits for a resource to become available
    while(*resource <= value){
        cond_wait(resource_cond, resource_mutex);
    }
    //Reserve resources according to the argument 'value'
    *resource = (value == 0) ? (*resource - 1) : (*resource - value);
    mutex_unlock(resource_mutex);
}

void signal_resource(uint *resource, uint value, pthread_mutex_t *resource_mutex, pthread_cond_t *resource_cond){
    mutex_lock(resource_mutex);
    //free resources according to the argument 'value'
    *resource = (value == 0) ? (*resource + 1) : (*resource + value);
    //signal to wake up asleep resource
    pthread_cond_signal(resource_cond);
    mutex_unlock(resource_mutex);
}

//Info Function
void print_order(Order order) {
    printf("\nID: %u\n", order.id);
    printf("Pizza quantity: %u\n", order.pizza_quantity);
    printf("Is Canceled: %d\n", order.is_canceled);
    printf("Payment Time: %f\n", order.payment_time);
    printf("Cooking Time: %f\n", order.cooking_time);
    printf("Baking Time: %f\n", order.baking_time);
    printf("Packing Time: %f\n", order.packing_time);
    printf("Delivery Time: %f\n", order.delivery_time);
    printf("Preparation Time: %f\n", order.packed_time);
    printf("Cooling Time %f\n", order.cooling_time);
    printf("Waiting Time %f\n", order.waiting_time);
    printf("=======================");
    }

//Error Handling Functions
void mutex_lock(pthread_mutex_t *lock){
    int rc = pthread_mutex_lock(lock);
    if(rc != 0){
        perror("Error: Failed to lock thread.\n");
        pthread_exit(&rc);
    }
}

void mutex_unlock(pthread_mutex_t *lock){
    int rc = pthread_mutex_unlock(lock);
    if(rc != 0){
        perror("Error: Failed to unlock thread.\n");
        pthread_exit(&rc);
    }
}

void clock_get_time_check(struct timespec *time){
    if(clock_gettime(CLOCK_REALTIME, time) != 0){
        perror("Error: Failed to calculate time.\n");
        exit(-1);
    }
}

void mutex_init(){
    if(pthread_mutex_init(&cooks_count_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for cooks.\n");
        exit(-1);
    }

     if(pthread_mutex_init(&ovens_count_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for ovens.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&packers_count_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for packers.\n");
        exit(-1);
    }

     if(pthread_mutex_init(&deliverers_count_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for deliverers.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&total_waiting_time_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for total waiting time.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&total_cooling_time_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for total cooling time.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&max_waiting_time_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for max waiting time.\n");
        exit(-1);
    }

     if(pthread_mutex_init(&max_cooling_time_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for max cooling time.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&min_waiting_time_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for min waiting time.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&min_cooling_time_mutex, NULL) != 0){
        perror("Error: Failed to initialize muetx for min cooling time.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&count_and_profit_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for pizzas count and profit.\n");
        exit(-1);
    }

    if(pthread_mutex_init(&satisfied_mutex, NULL) != 0){
        perror("Error: Failed to initialize mutex for staisfaction.\n");
        exit(-1);
    }

       if(pthread_mutex_init(&lockscreen_mutex, NULL) != 0){
        perror("Error: Could not initialize mutex for lockscreen.\n"); 
        exit(-1);
    }
}

void mutex_destroy(){
      if(pthread_mutex_destroy(&cooks_count_mutex) != 0){
        perror("Error: Failed to destroy mutex for cooks.\n");
        exit(-1);
    }

      if(pthread_mutex_destroy(&ovens_count_mutex) != 0){
        perror("Error: Failed to destroy mutex for ovens.\n");
        exit(-1);
    }


    if(pthread_mutex_destroy(&packers_count_mutex) != 0){
        perror("Error: Failed to destroy mutex for packers.\n");
        exit(-1);
    }

    if(pthread_mutex_destroy(&deliverers_count_mutex) != 0){
        perror("Error: Failed to destroy mutex for deliverers.\n");
        exit(-1);
    }

       if(pthread_mutex_destroy(&total_waiting_time_mutex) != 0){
        perror("Error: Failed to destroy mutex for total waiting time.\n");
        exit(-1);
    }

    if(pthread_mutex_destroy(&total_cooling_time_mutex) != 0){
        perror("Error: Failed to destroy mutex for total cooling time.\n");
        exit(-1);
    }


    if(pthread_mutex_destroy(&max_waiting_time_mutex) != 0){
        perror("Error: Failed to destroy mutex for max waiting time.\n");
        exit(-1);
    }

     if(pthread_mutex_destroy(&max_cooling_time_mutex) != 0){
        perror("Error: Failed to destroy mutex for max cooling time.\n");
        exit(-1);
    }

    if(pthread_mutex_destroy(&min_waiting_time_mutex) != 0){
        perror("Error: Failed to destroy mutex for min waiting time.\n");
        exit(-1);
    }

    if(pthread_mutex_destroy(&min_cooling_time_mutex) != 0){
        perror("Error: Failed to destroy mutex for min cooling time.\n");
        exit(-1);
    }

     if(pthread_mutex_destroy(&count_and_profit_mutex) != 0){
        perror("Error: Failed to destroy mutex for pizzas count and profit.\n");
        exit(-1);
    }

    if(pthread_mutex_destroy(&satisfied_mutex) != 0){
        perror("Error: Failed to destroy mutex for satisfaction.\n");
        exit(-1);
    }

    if(pthread_mutex_destroy(&lockscreen_mutex) != 0){
        perror("Error: Failed to destroy mutex for lockscreen.\n");
        exit(-1);
    }
}

void cond_init(){
    if(pthread_cond_init(&cooks_cond, NULL) != 0){
        perror("Error: Failed to initialize condition variable for cooks.\n");
        exit(-1);
    }

    if(pthread_cond_init(&ovens_cond, NULL) != 0){
        perror("Error: Failed to initialize condition variable for ovens.\n");
        exit(-1);
    }

    if(pthread_cond_init(&packers_cond, NULL) != 0){
        perror("Error: Failed to initialize condition variable for packers.\n");
        exit(-1);
    }

     if(pthread_cond_init(&deliverers_cond, NULL) != 0){
        perror("Error: Failed to initialize condition variable for deliverers.\n");
        exit(-1);
    }
}

void cond_wait(pthread_cond_t *cond, pthread_mutex_t *lock){
    if(pthread_cond_wait(cond, lock) != 0){
        perror("Error: Failed to wait.\n");
        exit(-1);
    }
}

void cond_destroy(){
    if(pthread_cond_destroy(&cooks_cond) != 0){
        perror("Error: Failed to destroy condition variable for cooks.\n");
        exit(-1);
    }

    if(pthread_cond_destroy(&ovens_cond) != 0){
        perror("Error: Failed to destroy condition variable for ovens.\n");
        exit(-1);
    }

    if(pthread_cond_destroy(&packers_cond) != 0){
        perror("Error: Failed to destroy condition variable for packers.\n");
        exit(-1);
    }

    if(pthread_cond_destroy(&deliverers_cond) != 0){
        perror("Error: Failed to destroy condition variable for deliverers.\n");
        exit(-1);
    }
}




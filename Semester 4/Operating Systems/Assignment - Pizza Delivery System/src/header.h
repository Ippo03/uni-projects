#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <time.h>
#include <math.h>
#include <unistd.h>
#include <stdbool.h>

//N = Number, T = Time, P = Probability, C = Cost

//CONSTANTS DECLARATION

//Resources
//Number of cooks
#define N_cook 2 
//Number of ovens    
#define N_oven 15
//Number of packers    
#define N_packer 2
//Number of deliverers
#define N_deliverer 10

//Randomizers edges
//Minimum time for an order to arrive
#define T_orderlow 1
//Maximum time for an order to arrive
#define T_orderhigh 3
//Minimum number of pizzas to be ordered
#define N_orderlow 1
//Maximum number of pizzas to be ordered
#define N_orderhigh 5
//Minimum time for an order to be examined(accepted or rejected)
#define T_paymentlow 1
//Maximum time for an order to be examined(accepted or rejected)
#define T_paymenthigh 3

//Probability of being a plain pizza
#define P_plain 60 
//Probability of an order to fail
#define P_fail 10

//Cost of a plain pizza
#define C_plain 10
//Cost of a special pizza
#define C_special 12

//Time for each stage of preparation
//Time needed for each pizza to be prepared
#define T_prep 1
//Time needed for each pizza to be baked
#define T_bake 10
//Time needed for each pizza to be packed
#define T_pack 1
//Minimum time needed for an order to be delivered
#define T_dellow 5
//Maximum time needed for an order to be delivered
#define T_delhigh 15

#define BILLION 1000000000.0

typedef unsigned int uint;

typedef struct {
    uint id;
    uint pizza_quantity;
    bool is_canceled;
    double payment_time;
    double cooking_time;
    double baking_time;
    double packing_time;
    double delivery_time;
    double packed_time;
    double cooling_time;
    double waiting_time;
} Order;

//FUNCTIONS DECLARATION

//routine function
void *order(void *order_id);

//info function
void print_order(Order order);

//helper functions
double get_time_difference(struct timespec *start_time, struct timespec *stop_time);
uint rand_generator(uint *seed, uint min, uint max);
void update_max_time(double new_time, pthread_mutex_t *max_mutex, double *cur_max);
void update_min_time(double new_time, pthread_mutex_t *min_mutex, double *cur_min);

//sychronization methods
void wait_for_resource(uint *resource, uint value, pthread_mutex_t *resource_mutex, pthread_cond_t *resource_cond);
void signal_resource(uint *resource, uint value, pthread_mutex_t *resource_mutex, pthread_cond_t *resource_cond);

//error handling functions
void mutex_lock(pthread_mutex_t *lock);
void mutex_unlock(pthread_mutex_t *lock);
void mutex_init();
void mutex_destroy();
void cond_init();
void cond_wait(pthread_cond_t *cond, pthread_mutex_t *lock);
void cond_destroy();
void clock_get_time_check(struct timespec *time);





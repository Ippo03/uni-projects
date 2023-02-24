import calendar
import datetime
from datetime import datetime
import csv

#global variables
#dictionary(key->date : value->(title,hour,duration))
events = {}
#calendar months in Greek
months = ["ΙΑΝ", "ΦΕΒ", "ΜΑΡ", "ΑΠΡ", "ΜΑΙ", "ΙΟΥΝ", "ΙΟΥΛ", "ΑΥΓ", "ΣΕΠ", "ΟΚΤ", "ΝΟΕ", "ΔΕΚ"]


#methods
#read csv file 
def read_csv():
    #show if there is or not an event in current date
    event_today = False
    #open csv file in read mode
    with open('events.csv', 'r') as csv_file:
        #get every line of csv file
        lines = csv_file.readlines()
        #read the file line by line skipping heading(Date,Hour,Duration,Title)
        for line in lines[1:]:
            #remove '""' from line
            line = line.replace('"','')
            #split the line into its values
            date, hour, duration, title = line.split(',')
            #remove '\n' from title
            title = title[:-1]
            #add event to the events dictionary using string date as key
            events[date] = (title,hour,duration)




#print calendar with format
def display(year, month):
    #create a calendar object
    cal = calendar.Calendar()
    #get a matrix of dates for a given month as a list of weeks (each week is a list of dates)
    month_cal = cal.monthdatescalendar(year, month)
    #print the calendar header using months list
    print(" _________________________________________________")
    print(f" {months[month-1]} {year}")
    print(" _________________________________________________")
    print("  ΔΕΥ |  ΤΡΙ |  ΤΕΤ |  ΠΕΜ |  ΠΑΡ |  ΣΑΒ |  ΚΥΡ")  
    #iterate over each week of the list
    for week in month_cal:
        #iterate over each day in the week
        for day in week:
            #day in the current month
            if day.month == month:
                #get the date as a string in the format "YYYY-MM-DD"
                date = str(day.year) + '-' + str(day.month) + '-' + str(day.day)
                #if key(date) exists in events dict->return date else->None
                event = events.get(date)
                #there is an event
                if event:
                    #day->single digit number
                    if (day.day >= 1 and day.day <= 9):
                        #print a space, the asterisk and the day's date in brackets if event
                        print(f"[ *{day.day:1}] |", end="")
                    #day->double digit-number
                    else:
                        #print the asterisk and the day's date in brackets if event(no space)
                        print(f"[*{day.day:2}] |", end="")
                #no event
                else:
                    #print the day's date only in brackets
                    print(f"[ {day.day:2}] |" , end="")
            #day not in current month
            else:
                #print days of other months with no brackets
                print(f"   {day.day:2} |", end="")
        print()




#convention
#date code-> 1
#hour code-> 2
#duration code-> 3
#title code-> 4
#validation of the input
def validate_input(input, code):
    #case->date
    if code == 1:
        try:
            #split date into year, month, day and convert to int
            in_year, in_month, in_day = map(int, input.split('-'))
            #make sure year > 2023
            if in_year < 2023:
                raise ValueError("Το έτος πρέπει να είναι μεγαλύτερο του 2022")
            #make sure month between 1 and 12
            if in_month < 1 or in_month > 12:
                raise ValueError("Ο μήνας πρέπει να είναι ανάμεσα στο 1 και στο 12")
            #make sure day between 1 and 31
            if in_day < 1 or in_day > 31:
                raise ValueError("Οι ημέρες πρέπει να είναι απο 1 μέχρι το πολύ 31")
            #print free hours for the specific day
            print(find_free_spaces(in_year, in_month, in_day))
        except ValueError as e:
            print("Λάνθασμένη είσοδος δεδομένων.Ξαναδοκίμασε.") 
            print()
            return False
    #case->hour
    elif code == 2:
        try:
            #split time into hours and minuts and convert to int
            h, min = map(int, input.split(':'))
            #make sure hours between 0 and 23
            if h < 0 or h > 23:
                raise ValueError("Η ώρα πρέπει να είναι ανάμεσα στο 0 και το 23")
            #make sure minutes between 0 and 59
            if min < 0 or min > 59:
                raise ValueError("Τα λεπτά πρέπει να είναι ανάμεσα στο 0 και το 59")
        except ValueError as e:
            print("Λάνθασμένη είσοδος δεδομένων.Ξαναδοκίμασε.") 
            print()
            return False
    #case->duration
    elif code == 3:
        try:
            #convert to int
            input = int(input)
            #make sure duartion is positive
            if input <= 0:
                raise ValueError("Η διάρκεια πρέπει να είναι θετικός αριθμός")
        except ValueError as e:
            print("Λάθασμένη είσοδος δεδομένων.Ξαναδοκίμασε.") 
            print()
            return False
    #case->title
    elif code == 4:
        try:
            #make sure title does not contain ','
            if "," in input:
                raise ValueError("Ο τίτλος δεν πρέπει να περιέχει κόμμα")
        except ValueError as e:
            print("Λάθασμένη είσοδος δεδομένων.Ξαναδοκίμασε.") 
            print()
            return False
    #every input OK->True
    return True




#get inputs when recording an event
def get_r_input():
    #local variable that shows if validation went correctly
    OK = False
    #no valid input->keep asking for date input
    while(OK != True):
        in_date = input("Εισάγετε ημερομηνία (YYYY-MM-DD): ")
        #use upper method to validate
        OK = validate_input(in_date, 1)
    OK = False
    #no valid input->keep asking for hour input
    while(OK != True):
        in_hour = input("Εισάγετε ώρα (HH:MM): ")
        #use upper method to validate
        OK = validate_input(in_hour, 2)
        #valid input not enough,also check if there is another event in this date and this time
        if check_schedule_conflicts(in_date, in_hour) and OK:
            print()
            print("Υπάρχει ήδη ένα γεγονός αυτή την στιγμή")
            print("Παρακαλώ δώστε άλλη ημερομηνία και ώρα")
            print()
            OK = False
        else:
            break
    OK = False
    #no valid input->keep asking for duration input
    while(OK != True):
        in_duration = input("Εισάγετε διάρκεια (minutes): ")
        #use upper method to validate
        OK = validate_input(in_duration, 3)
    OK = False
    #no valid input->keep asking for title input
    while(OK != True):
        in_title = input("Εισάγετε τίτλο: ") 
        #use upper method to validate
        OK = validate_input(in_title, 4)
    #return validated information of the event about to be recorded
    return in_date, in_hour, in_duration, in_title




#get inputs when updating an event
def get_u_input(ev_date, ev_hour, ev_duration, ev_title):
    #local variable that shows if validation went correctly
    OK = False
    #no valid input->keep asking for date input
    while(OK != True):
        #input = a new date or the same date if user presses enter button
        new_date = input(f"Ημερομηνία γεγονότος ({ev_date}): ") or ev_date
        #use upper method to validate
        OK = validate_input(new_date, 1)
    OK = False
    #no valid input->keep asking for time input
    while(OK != True):
        #input = a new time or the same time if user presses enter button
        new_hour = input(f"Ώρα γεγονότος ({ev_hour}): ") or ev_hour
        #use upper method to validate
        OK = validate_input(new_hour, 2)
        #valid input not enough,also check if there is another event in this date and this time
        if check_schedule_conflicts(new_date, new_hour) and OK:
            print()
            print("Υπάρχει ήδη ένα γεγονός αυτή την στιγμή")
            print("Παρακαλώ δώστε άλλη ημερομηνία και ώρα")
            print() 
            OK = False
        else:
            break
    OK = False
    #no valid input->keep asking for duration input
    while(OK != True):
        #input = a new duration or the same duration if user presses enter button
        new_duration = input(f"Διάρκεια γεγονότος ({ev_duration}): ") or ev_duration
        #use upper method to validate
        OK = validate_input(new_duration, 3)
    OK = False
    #no valid input->keep asking for title input
    while(OK != True):
        #input = a new title or the same title if user presses enter button
        new_title = input(f"Τίτλος γεγονότος ({ev_title}): ") or ev_title
        #use upper method to validate
        OK = validate_input(new_title, 4)
    #return validated information of the event about to be updated
    return new_date, new_hour, new_duration, new_title




#record an event
def record_event():
    #get validated inputs using get_r_input method
    r_date, r_hour, r_duration, r_title = get_r_input()
    #add them in the dictionary
    events[r_date] = (r_title, r_hour, r_duration)




#print events numbered of a month given in a year given,return them in a list
def search_events():
    print("=== Αναζήτηση γεγονότων ===")
    #prompt the user for the year and month
    in_year = int(input("Εισάγετε έτος: "))
    in_month = int(input("Εισάγετε μήνα: "))
    #counter
    event_num = 0
    #events of this month and year
    event_dates = []
    #iterate over each key of the dictionary
    for date in events.keys():
        #split date into year, month, day and convert to int
        year, month, day = map(int, date.split('-'))
        #year and month of iteration matches with month and year given->there is an event 
        if in_year == year and in_month == month:
            #get events details(value->(title,hour,duration))
            event_title, event_hour, event_duration = events[date]
            #print formatted
            print(f"{event_num}. [{event_title}] -> Date: {date}, Time: {event_hour}, Duration: {event_duration}")
            #add event in the list
            event_dates.append(date)
            #increase counter by 1
            event_num += 1
    return event_dates




#delete an event
def delete_event():
    #get a list of events numbered using search_events method
    event_dates = search_events()
    #prompt the user for the event number to be deleted
    event_num = int(input("Επιλέξτε γεγονός προς διαγραφή: "))
    #get the event date using the event number as an index
    date = event_dates[event_num]
    #delete event from dictionary
    del events[date]




#update an event
def update_event():
    #get a list of events numbered using search_events method
    event_dat = search_events()
    #prompt the user for the event number
    event_num = int(input("Επιλέξτε γεγονός προς ενημέρωση: "))
    #get the event date using the event number as an index
    date = event_dat[event_num]
    #get the current event details
    event_title, event_hour, event_duration = events[date]
    #get validated inputs using get_u_input method
    new_date, new_hour, new_duration, new_title = get_u_input(date, event_hour, event_duration, event_title)
    #delete old event
    del events[date]
    #add new events in the dictionary
    events[new_date] = (new_title, new_hour, event_duration)
    #print a confirmation message
    print(f"Το γεγονός ενημερώθηκε: <{new_title} -> Date: {date}, Time: {new_hour}, Duration: {new_duration}>")
    print()




#check if there is an event today
def todays_event():
    #get current date
    cur_date = datetime.now().date()
    #list with today's event
    current_day_events = []
    #boolean->event today
    event_today = False
    #iterate over each key of the dictionary
    for date in events.keys():
        #split date string into a year, month, and day and convert to int
        year, month, day = map(int, date.split('-'))
        #date matches with current date->there is an event today
        if year == cur_date.year and month == cur_date.month and day == cur_date.day:
            event_today = True
            #add event in the list
            current_day_events.append(date)
    #have at least one event
    if event_today:
        #iterate today's events
        for event in current_day_events:
            #split time into hours and minutes and convert to int
            event_hour, event_minute = map(int,((events[event])[1]).split(':'))
            #create a datetime object with the event start time
            event_time = datetime.now().replace(hour=event_hour, minute=event_minute)
            #calculate the time difference between the event start time and now
            time_diff = event_time - datetime.now()
            hours, remainder = divmod(int(time_diff.total_seconds()), 3600)
            minutes, seconds = divmod(remainder, 60)
            #print a notification message with the time difference
            print(f'Ειδοποίηση: σε {hours} ώρες και {minutes} λεπτά από τώρα έχει προγραμματιστεί το γεγονός {(events[event])[0]}!!!')
    #no event today
    else:
        print("Δεν έχει προγραμματιστεί κάποιο γεγονός για σήμερα")




#find free spaces within a day of a date given
def find_free_spaces(year, month, day):
    #list with free spaces as tuples
    free_spaces = []
    #boolean->event in this date
    event_found = False
    #iterate over each item of the dictionary
    for event_date, event_details in events.items():
        #split date string into a year, month, and day and convert to int
        ev_year, ev_month, ev_day = map(int, event_date.split('-'))
        #no event in input date,continue
        if ev_year != year or ev_month != month or ev_day != day:
            continue
        #found event
        event_found = True
        #split time into hours and minutes and convert to int
        event_start_hour, event_start_minute = map(int, event_details[1].split(':'))
        #convert duration to int
        event_duration = int(event_details[2])
        #calculate start and end time of the event
        event_end_hour = event_start_hour + event_duration // 60
        event_end_minute = event_start_minute + event_duration % 60
        #check if event_end_minute is greater than or equal to 60 and adjust the end time accordingly
        if event_end_minute >= 60:
            event_end_minute -= 60
            event_end_hour += 1
        #check if the event starts at midnight
        if event_start_hour == 0 and event_start_minute == 0:
            free_start_time = "00:00"
            free_end_time = f"{event_start_hour}:{event_start_minute}"
            free_spaces.append((free_start_time, free_end_time))
        else:
            free_start_time = "00:00"
            free_end_time = f"{event_start_hour}:{event_start_minute}"
            free_spaces.append((free_start_time, free_end_time))
        #check if the event ends at midnight
        if event_end_hour == 23 and event_end_minute == 59:
            free_start_time = "23:59"
            free_spaces.append((free_start_time, event_details[1]))
        else:
              free_start_time = f"{event_end_hour}:{event_end_minute}"
              free_end_time = "23:59"
              free_spaces.append((free_start_time, free_end_time))
    #this day is free of events
    if not event_found:
        return "Δεν βρέθηκαν εκδηλώσεις στην ημερομηνία.Επιλέξτε όποια ώρα επιθυμήτε"
    return free_spaces




#check if input conflicts with pre-existing event
def check_schedule_conflicts(date, hour):
    #iterate over each item of the dictionary
    for event_date, event_details in events.items():
        #no conflict
        if event_date != date:
            continue
        #split time into hours and minutes and convert to int
        event_start_hour, event_start_minute = map(int, hour.split(':'))
        #calculate the end time of the new event based on its start time and duration
        event_end_hour = event_start_hour
        event_end_minute = event_start_minute + int(event_details[2])
        #calculate the end time of the existing event based on its start time and duration
        existing_event_start_hour, existing_event_start_minute = map(int, event_details[1].split(':'))
        existing_event_end_hour = existing_event_start_hour
        existing_event_end_minute = existing_event_start_minute + int(event_details[2])
        #check if the new event overlaps with the existing event
        if event_start_hour < existing_event_end_hour or (event_start_hour == existing_event_end_hour and event_start_minute < existing_event_end_minute):
            if existing_event_start_hour < event_end_hour or (existing_event_start_hour == event_end_hour and existing_event_start_minute < event_end_minute):
                return True
    return False




#write updated events in csv file
def write_csv():
    #open csv in write mode
    with open("events.csv", "w", newline="", encoding="utf-8") as save_file:
        writer1 = csv.writer(save_file,delimiter=",")
        #re-write heading
        writer1.writerow(["Date,Hour,Duration,Title"])
        #iterate over each items of the dictionary
        for date,info in events.items():
            #print event with commas
            writer1.writerow([str(date) + ',' + str(info[1]) + ',' + str(info[2]) + ',' + str(info[0])])




#read file
read_csv()
#get current date
current_date = datetime.now()
cur_year, cur_month, current_day = current_date.year, current_date.month, current_date.day
while True:
  print()
  #print if there is an event today or not
  todays_event()
  #display calendar using current year and month
  display(cur_year, cur_month)
  print()
  #print main menu and ask for user's choice
  user_input = input("""Πατήστε ENTER για προβολή του επόμενου μήνα, "q" για έξοδο ή κάποια από τις 
παρακάτω επιλογές:
      "-" για πλοήγηση στον προηγούμενο μήνα
      "+" για διαχείριση των γεγονότων του ημερολογίου
      "*" για εμφάνιση των γεγονότων ενός επιλεγμένου μήνα
      ->""")
  #(+)->go to submenu
  if user_input == "+":
    while True:
      print(  "----------------------------------------------------")
      print("Διαχείριση γεγονότων ημερολογίου, επιλέξτε ενέργεια:")
      print("    1 Καταγραφή νέου γεγονότος")
      print("    2 Διαγραφή γεγονότος")
      print("    3 Ενημέρωση γεγονότος")
      print("    0 Επιστροφή στο κυρίως μενού")
      print("    -> ", end="")
      #ask agaia for user's input
      user_input = input()
      #1->record event
      if user_input == "1":
        #use appropriate method
        record_event()
        print()
        pass
      #2->delete event
      elif user_input == "2":
        print()
        #use appropriate method
        delete_event()
        print()
        #press any key to return
        print("Πατήστε οποιοδήποτε χαρακτήρα για επιστροφή στο κυρίως μενού: ") 
        user_input = input()
        if(user_input):
          break 
        pass
      #3->upadte event
      elif user_input == "3":
        print()
        #use appropriate method
        update_event()
        print()
        #press any key to return
        print("Πατήστε οποιοδήποτε χαρακτήρα για επιστροφή στο κυρίως μενού: ") 
        user_input = input()
        if(user_input):
          break 
        pass
      #0->return to main menu
      elif user_input == "0":
        break
      else:
        print("Λάθασμένη είσοδος δεδομένων.Ξαναδοκίμασε.")
        print()
    pass
  #(-)->#display the previous month
  elif user_input == "-":
    if(cur_month == 1):
        cur_month = 12
        cur_year = cur_year - 1
    else:
      cur_month = cur_month - 1
    pass
  #(*)#show every event of the year and month that user chose
  elif user_input == "*":
    #use appropriate method
    search_events()
    print()
    #press any key to return
    print("Πατήστε οποιοδήποτε χαρακτήρα για επιστροφή στο κυρίως μενού: ")
    user_input = input()
    if(user_input):
      break
    pass
  #(q)->quit digital diary
  elif user_input == "q":
    #update changes in csv file
    write_csv()
    #exit the program
    break
  #(any other key)->display the next month
  else:
    if(cur_month == 12):
        cur_month = 1
        cur_year = cur_year + 1
    else:
      cur_month = cur_month + 1
    pass

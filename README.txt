TITLE:
Scheduling application

PURPOSE:
Provide scheduling capabilities based on business requirements using a persistent data source with a
GUI-based application. The application can create, modify and delete customers; create, modify and
delete appointments; and generate a variety of reports according to business needs. The application is
multi-language capable and uses best practices for database security.

AUTHOR/CONTACT INFORMATION:
Rebecca Fredricks
rfredr2@wgu.edu

VERSION/DATE CREATED:
Student application version 1.2.0
Date created: 12 SEP 2021

IDE & JAVA VERSION INFORMATION:
IntelliJ IDEA 2021.1.1 Community Edition
JDK 11.0.11
JavaFX-SDK-11.0.2

HOW TO RUN THE PROGRAM:
Upon launching the program, a login screen will appear. Log in to the program by entering
"test" for both username and password and click "login". The main menu will load.
Make a selection from the main menu to view customers, view appointments, or view reports.
Return to the main menu at any time by selecting "main menu" from any screen.

To add a new customer, navigate to the customers menu and select "add new". To edit
an existing customer or delete a customer record, highlight the customer in the table
and select "modify" or "delete". When adding or modifying a customer, fill in all the
required information before choosing "save" or choose "cancel" to return.

To add a new appointment, navigate to the appointments menu and select "create new". To
edit an existing appointment or delete an appointment record, highlight the appointment
in the table and select "edit appointment" or "cancel appointment". When creating a new
appointment or editing an existing appointment, fill in all the required information before
choosing "save" or choose "cancel" to return. When selecting or changing date and time for
an appointment, you must select start date and start time before end time can be selected.

To view a calendar of appointments, navigate to the appointments menu and use the "change
calendar view" dropdown menu. Select "view calendar week" to view the next 7 days or
"view calendar month" to view the next 30 days. Select "view all" to show all appointments.

To view reports, navigate to the reports menu and select a report from the radio buttons
at the bottom of the screen. The report will appear onscreen.To view a different
report, choose a different radio button.

To exit the program, choose "exit" from the main menu.

ADDITIONAL REPORT AVAILABLE:
The additional report available is a summary of customer appointments within the last 30 days.

JDBC DRIVER & CONNECTION STRING
JDBC driver version 8.0.25
Please ensure the database connection string has "?connectionTimeZone=SERVER" appended.
# beach volley courts booking system

This app is used to book and manage the availability of public beach volleyball courts. It is envisioned to include a user identity system that allows users to view court availability, book a court for one or more hours, schedule or make payments, cancel reservations, and potentially purchase merchandise or request/renew association membership.

Administrators will have the ability to confirm reservation requests, validate completed payments, and insert course schedules that occupy the courts.

## v0.1:
* Data model implementation
* Data manipulation via REST API
* List of available time slots
* Booking functionality

### Data Model:
* users (name, surname, email, password, membershipExpiryDate, medicalCertificateExpiryDate, isAdmin)
* schedules (id, date, startTime, endTime, bookings {name, startTime, endTime, user, payment (reference?), playerList{name, surname}, isAproved, notes, cancellationDate, cancellationNotes})

## v1.0:
* Administrative Options (Booking Confirmation, Payment Confirmation, Direct Course Input, Forced Booking Cancellation)
* Graphic interface
* Weekend Reservation Requests

## v2.0:
* Mobile implementation
* Login and User/Admin Management
* User registration
* Summary of Personal Reservations
* Reservation Cancellation
* Integration with Google Calendar
* Integration with Payment Systems (?)
* Adding payments (date, user, amount, type, isConfirmed, confirmedBy) to the model
* Reservation Confirmation Notifications (Push Notification / Email / SMS)
## v3.0:
* Merchandising
* Membership Request/Renewal
* Automatic generation and sending of Payment Receipt Documents



# ¡todo:
* Administrative Options (Booking Confirmation, Payment Confirmation, Direct Course Input, Forced Booking Cancellation)
* Graphic interface
* when you create a booking you should test the maximum capacity of a schedule slot
* find schedules by date, time period?
* filter schedules by only free slots?
* Weekend Reservation Requests
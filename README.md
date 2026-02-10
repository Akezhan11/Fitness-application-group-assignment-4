# Fitness Club Membership & Class Booking
Akezhan Muzamelov ,Tauyekeluly Shalkar

Course name: Object Orientated Programming (Java)

Course name: Object Orientated Programming (Java)

Course instructor: Traxel Xeniya

Course trimester / year: 2 / 1

Endterm

Assignment name: Project defense

# PROJECT TOPIC AND MAIN USER FLOWS
Project title: Fitness-application (Gym / Fitness center management system)

Project type: Console-based Java application with PostgreSQL (Supabase) database.

The application is a small management system for a fitness center. It allows an operator (or user) to manage members, memberships, fitness classes, class bookings, and attendance. The program runs in console, shows a menu, reads commands from user input, and calls the service layer. Services use repositories to read/write data in the database via JDBC.

Main user flows in the console menu:
Fitness classes flow: The user can list all fitness classes, find a class by type, find a class by id, filter classes by trainer name and surname, filter classes by cost, add a new class, book a class, cancel a booking, and show bookings for a chosen fitness class. Booking flow checks that the member exists, the class exists, membership is active, the class is not full, and the booking does not already exist.

Membership flow: The user can buy membership for a member, check if membership is active, update membership, deactivate membership, and retrieve membership details by member id.

Members flow: The user can add a member, show all members, find by id, find by email, find by phone, update member profile, and show bookings by member id.
Bookings overview flow: The user can list all bookings in the system.

Attendance flow (added): The user can mark attendance for a member for today, mark attendance for a custom date, and show attendance records by member id. Attendance is stored in a separate table and is linked to a member.

# DATABASE SCHEMA (ENTITIES, TABLES, RELATIONSHIPS)
The database is PostgreSQL. Tables are created (if not exists) at startup using a DB initialization class.

Core entities and tables:
Members table: stores member personal data. Columns include id (primary key), name, surname, phone, email (unique), gender. Each member is uniquely identified by id. Email uniqueness prevents duplicates.

Fitness (classes) table: stores fitness class sessions (or class definitions depending on implementation). Columns include id (primary key), type, description, date, time, cost, trainer_first_name, trainer_last_name, max_places. This table represents fitness classes that can be booked.

Bookings table: stores the booking events. Columns include id (primary key), member_id (foreign key to members.id), class_id (foreign key to fitness.id), booking_date (timestamp default current). This creates a many-to-many relationship between members and fitness classes through bookings. One member can book many classes; one class can have many bookings (until max_places).

Memberships table: stores membership status per member. Columns include id (primary key), member_id (unique + foreign key), type, start_date, end_date, active (boolean). The unique constraint on member_id means one membership record per member at a time (logical 1-to-1). The foreign key has ON DELETE CASCADE so when a member is deleted, their membership is deleted too.

Attendance table (added): stores visit/attendance records. Recommended structure: id (primary key), member_id (foreign key to members.id), visit_date (date). The logical relationship is one-to-many: one member can have many attendance records. A practical rule is to avoid duplicates for the same member and date (unique(member_id, visit_date)) so the same day is not counted twice.

Relationships summary:
Members 1 — N Bookings, Fitness 1 — N Bookings (bookings is a junction table).
Members 1 — 1 Memberships (enforced by UNIQUE(member_id)).
Members 1 — N Attendance.

# ARCHITECTURE (PACKAGES, MAIN CLASSES, USED PATTERNS)
The project is organized into packages to separate responsibilities and keep code maintainable.

entities package: contains domain models such as Member, FitnessClass, ClassBooking, MembershipType, Attendance. These classes represent the business data used across the application.

edu.aitu.oop3.db package: contains database infrastructure. DatabaseConnection provides JDBC connections to PostgreSQL (Supabase). DataBaseCreation (DB init) creates required tables at startup.

repositories package: contains repository interfaces (contracts) such as MemberRepository, FitnessClassRepository, MembershipRepository, AttendanceRepository, ClassBookingRepository. Interfaces define the operations (save, find, update, findAll, etc.).
repositories.implementations package: contains JDBC implementations such as DbMemberRepository, DbFitnessClassRepository, DbMembershipRepository, DbClassBookingRepository, DbAttendanceRepository. Each implementation contains SQL queries and mapping from ResultSet to entity objects.

service package: contains business logic services such as MemberService, FitnessClassService, MembershipService, BookingService, AttendanceService. Services validate rules and orchestrate multiple repositories.

exception package: contains custom exceptions for business rules, for example BookingAlreadyExistsException, ClassFullException, MembershipExpiredException.
factories package (added/used): contains MembershipFactory and MembershipKind. This is used to create membership objects/behavior in a consistent way based on membership kind (MONTHLY, YEARLY, VISIT_BASED).

utils package (added/used): contains Filter interface and ListUtils. ListUtils provides a generic filter method to apply a predicate-like condition over lists, keeping reusable list logic outside services.

Main entry and application flow:
Main class is the console controller. It initializes repositories, then services, then enters a loop to show menus and handle user commands. When the app starts, it first checks DB connection, runs table initialization, and only then starts the interactive console. Main does not directly execute SQL; it delegates to services and repositories.

Used patterns:
Repository pattern: repository interfaces abstract data access; JDBC implementations isolate SQL from business logic.

Service layer pattern: services encapsulate business rules (membership checks, booking restrictions, attendance validation) and coordinate repositories.

Factory pattern (membership): MembershipFactory creates membership configuration based on MembershipKind, reducing conditional logic scattered across the code and centralizing creation rules.

Singleton pattern (database): DatabaseConnection is implemented as a singleton instance so all code uses the same configuration and connection creation logic consistently.
Separation of concerns (layering): UI (Main) → Service → Repository → DB. Each layer has one main responsibility.

# COMPONENTS AND HOW THEY FOLLOW REP / CCP / CRP

This section explains how the code organization follows package design principles.

REP (Reuse/Release Equivalence Principle):
Packages are grouped by reusable responsibility. For example, utils (Filter, ListUtils) is a reusable helper unit that can be reused by different services and future features. factories (MembershipFactory, MembershipKind) is another reusable unit: membership creation rules are centralized and can be reused anywhere membership is created or recalculated. The db package is a reusable unit for DB setup and connection logic across repositories.

CCP (Common Closure Principle):
Classes that change for the same reason are kept together. SQL changes (table/column rename, query updates) mostly affect repositories. Therefore repository implementations are in one package, so database-related changes do not spread into service or UI code. Business rule changes (for example, new booking rule, membership validation rule, attendance constraints) affect the service package, not the repositories. UI changes (menu text, new menu items) affect the Main class. This reduces the number of packages touched by a single change.

CRP (Common Reuse Principle):
Classes that are reused together are packaged together, and unrelated classes are kept apart to avoid forcing unnecessary dependencies. For example, a service that uses filtering can reuse utils without importing DB packages. Repositories can use db (DatabaseConnection), but entities and utils do not depend on repositories. This prevents “fat” packages where importing one class forces bringing many unrelated ones. Attendance was added as its own entity + repository + service; it does not force modifications inside unrelated parts except where the menu connects it (Main).

Recent structural improvements added to architecture:
Attendance feature was introduced as a separate component (Attendance entity, AttendanceRepository interface, DbAttendanceRepository implementation, AttendanceService, and menu options). This follows the same layering rules and keeps changes localized.
MembershipFactory and MembershipKind were introduced to centralize membership creation logic and reduce duplication, improving maintainability.
Filter and ListUtils were added to provide reusable list filtering functionality and avoid repeating loops/conditions inside services.

# CONCLUSION
Fitness-application is a console-based Java system for gym management using a layered architecture and a relational database. The project supports core workflows for managing members, memberships, fitness classes, bookings, and attendance. The architecture applies Repository and Service patterns to separate data access from business rules, uses a Singleton for database connection configuration, and introduces a Factory for membership creation. The package structure supports REP/CCP/CRP by grouping code by responsibilities and change reasons, making the system easier to extend (such as adding attendance) without breaking existing modules.
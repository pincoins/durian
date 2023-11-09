# Spring Boot based Pincoin backend

## Persistence
- Spring Data JPA (JPQL, Native Query, Procedure)
- Query DSL (Select)
- EntityManager (HQL)
- JDBC template (Bulk Insert)

## Checked/Unchecked Exceptions
Domain entity level integrity/consistency
- throw in domain models - caught in service

Business logic validation
- throw in service - caught in ResponseEntityExceptionHandler

Request DTO validation
- throw in controller - caught in ResponseEntityExceptionHandler

## Transactions

## Schedulers

## Verification
- Email
- Phone
- Document (photo ID, credit card, bank account book)
- Bank account 1 won

## 3rd-party
- AWS S3
- Aligo
- Line-notify
- Mailgun
- Galaxia Billgate
- PayPal
- Danal Phone Verification

## Message board
- Notice
- FAQ
- Testimonials
- Qna

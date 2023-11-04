# Spring Boot based Pincoin backend

## Persistence
- Spring Data JPA (JPQL, Native Query, Procedure)
- Query DSL (Select)
- EntityManager (HQL)
- JDBC template (Bulk Insert)

## Checked/Unchecked Exceptions
- throw in domain models - caught in service
- throw in service - caught in ResponseEntityExceptionHandler
- throw in controller - caught in ResponseEntityExceptionHandler

## Transactions

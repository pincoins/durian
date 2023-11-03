# Spring Boot based Pincoin backend

## Persistence
- Spring Data JPA (JPQL, Native Query, Procedure)
- Query DSL (Select)
- EntityManager (HQL)
- JDBC template (Bulk Insert)

## Checked/Unchecked Exceptions
- throw in domain models - catch in service
- throw in service - catch in ResponseEntityExceptionHandler
- throw in controller - catch in ResponseEntityExceptionHandler

## Transactions

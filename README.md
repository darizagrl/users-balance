# Project Overview
This is a Kotlin application built with Spring Boot.   
It follows a 3-layer architecture style and includes a single REST endpoint: `PUT /users/balances`.  
This endpoint achieves the second level of maturity in the Richardson Maturity Model. HATEOAS support is not relevant for this application due to its single endpoint design.

## Endpoint Details
The `PUT /users/balances` endpoint accepts a payload of type `Map<UUID, Int>`. In this map, the key represents the user ID as a `UUID`, and the value represents the balance of the user's wallet. This design choice was made to adopt the commonly used UUID standard for user IDs instead of using integers, which were initially used in a `Map<Int, Int>` format.

![image](https://github.com/darizagrl/users-balance/assets/31920567/8ce13524-14e4-4710-ae88-2f6b8eaa01f0)

## Database Structure
The requirement was to use a PostgreSQL database with a user table containing the following fields: `ID`, `name`, and `balance`.
To optimize the database design, this has been divided into two tables: `User` and `Balances`. I've added additional `created_at` and `updated_at` fields, to use them for querying and partitioning. 

![image](https://github.com/darizagrl/users-balance/assets/31920567/fc7116c4-1c57-4b53-a0a5-fb7fa53af339)

Additionally, the `Balances` table is partitioned by the `created_at` range for improved query performance. An index on `userId` has also been added to enhance query efficiency.

![image](https://github.com/darizagrl/users-balance/assets/31920567/26b2af86-382e-46ca-bc63-2d41fee15b86)

## Performance Optimization
The main challenge was handling up to a million records efficiently. To address this, the following optimizations were implemented:

* **Batch Updates:** Used for efficient data insertion.
* **Overriding Hibernate's `isNew()` Function:** Removed additional selects before inserting new records.
* **Second-Level Cache with EhCache:** Enabled to improve data retrieval performance. Used the READ_WRITE strategy.  
   Added `@Cacheable(value = ["balanceCache"], key = "#userIds")` to cache queries and optimized querying by `updatedAt` parameter.
  
  ```kotlin
   @Query("SELECT b FROM Balance b WHERE b.userId IN :userIds AND b.updatedAt BETWEEN :start AND :end")
    fun findBalancesByUserIdsAndUpdatedAtBetween(
        @Param("userIds") userIds: Collection<UUID>,
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime
    ): List<Balance>
  ```
* **Data Partitioning:** Implemented partitions by `created_at` date for the `Balances` table.

Initially, data was partitioned by `userId` ranges, and query optimization was done based on these ranges. However, with the switch to `UUID`'s for `userId`'s, this approach was no longer applicable as the sorting of `UUID` strings does not provide additional optimization benefits.

  ```kotlin
@Query("SELECT b FROM Balance b WHERE b.userId >= :startUserId AND b.userId <= :endUserId")
    fun findByUserIdRange(@Param("startUserId") startUserId: Int, @Param("endUserId") endUserId: Int): List<Balance>
  ```

* **Connection Pooling:** Configured HikariCP with `spring.datasource.hikari.maximum-pool-size=50` to efficiently manage database connections.
* **SQL Request Statistics:** Enabled to check reports on queries using `spring.jpa.properties.hibernate.generate_statistics=true`.
* **Logging Interceptor:** Implemented a `BeanPostProcessor` that logs executed query information using SLF4J. Although this exposes parameter values in logs, which is not recommended for a production application, it can be annotated with `@Profile("local")`. But as it's a test task, only the default profile was used.

## Testing
The application is covered with basic unit tests.  
Performance testing can be conducted with JMeter, but it requires additional effort and is not within the scope of this task.  
However, it could be considered a future improvement.

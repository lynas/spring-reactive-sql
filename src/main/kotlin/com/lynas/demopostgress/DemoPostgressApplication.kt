package com.lynas.demopostgress

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@SpringBootApplication
class DemoPostgressApplication

fun main(args: Array<String>) {
    runApplication<DemoPostgressApplication>(*args)
}

@Table("customer")
data class Customer(
    @Id
    val id: Long,
    val name: String
)

interface CustomerRepository : ReactiveCrudRepository<Customer, Long> {

    suspend fun findByName(name: String) : Mono<Customer>

    @Query("select c.* from customer c where c.name=?")
    suspend fun findByCustomerName(name: String) : Mono<Customer>
}

@RestController
@RequestMapping("/customer")
class CustomerController(val customerRepository: CustomerRepository) {

    @GetMapping
    suspend fun getAllCustomer() : Flow<Customer> = customerRepository.findAll().asFlow()

    @GetMapping("/{id}")
    suspend fun getOneCustomer(@PathVariable id: Long) : Customer? = customerRepository.findById(id).awaitFirstOrNull()

    @GetMapping("/name/{name}")
    suspend fun getCustomerByName(@PathVariable name: String) : Customer? = customerRepository.findByName(name).awaitFirstOrNull()

    @GetMapping("/cname/{name}")
    suspend fun getCustomerByCustomerName(@PathVariable name: String) : Customer? = customerRepository.findByCustomerName(name).awaitFirstOrNull()


}
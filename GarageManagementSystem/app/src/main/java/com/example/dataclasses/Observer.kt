package com.example.dataclasses

interface Observer {
    fun update(data : Any) // Implement the update() method upon data changes
}
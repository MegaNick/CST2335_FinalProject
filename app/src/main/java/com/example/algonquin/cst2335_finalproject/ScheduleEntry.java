//To the Glory of God!
// Thermostat activity by Nikolay Melnik. Algonquin College. Ottawa, 2018.
//
package com.example.algonquin.cst2335_finalproject;

/**
 * Transfer Object class for Thermostat Activity
 * By Nikolay Melnik
 */
class ScheduleEntry {
    int id, day, hours, minutes, temperature;

    /**
     * Default constructor
     */
    public ScheduleEntry() {}

    /**
     * Creates ScheduleEntry object from parameters
     * @param a id
     * @param b day
     * @param c hours
     * @param d minutes
     * @param e temperature
     */
    public ScheduleEntry (int a, int b, int c, int d, int e){
        this.id = a;
        this.day = b;
        this.hours = c;
        this.minutes = d;
        this.temperature = e;
    }
}

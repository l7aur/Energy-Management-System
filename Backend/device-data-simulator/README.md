# Device Data Simulator

C++ CLI standalone application that simulates consumption sensor readings patterns for common household devices.

## Description

`DeviceDataSimulator.exe` generates sensor readings for some common home devices. The timestamps of the readings are generated with a 10 minute increment over up to 24 hours (`00:00:00` → `23:50:00`).

A sensor reading corresponds to a JSON object that is placed in a RabbitMQ queue running at `localhost:5672` (RabbitMQ default port).

  ```json
  {
      "timestamp"     : {
            "year"    : <number:uint>, // <= 2025
            "month"   : <number:uint>, // <= 12
            "day"     : <number:uint>, // dependent on the value of month [01 = JAN, 02 = FEB, ...]
            "hour"    : <number:uint>,
            "minute"  : <number:uint>,
            "second"  : <number:uint>
      },
      "deviceId"      : <number:uint>,
      "measuredValue" : <number:float> // always positive
  }
  ```

## Usage

```ps1
  .\DeviceDataSimulator.exe <device_id> <device_type> <number_of_readings>
  <device_id>: UINT > 0
  <device_type>: [TV, PC, Fridge, CoffeeMachine, Laptop]
  <number_of_readings>: UINT < 145
```

The generated values aim to mimic real-life consumption scenarions for different home devices.

## Tech Stack

| Component  | Technology     |
|------------|----------------|
| Language   | C++20          |
| Compiler   | MSVC           |
| Deployment | exe (CLI Tool) |

- This project links statically [AMQP-CPP](https://github.com/CopernicaMarketingSoftware/AMQP-CPP) to facilitate interaction with RabbitMQ.

## Notes

- Measurements are partially pseudo-random generated, but the seed is preset to `NULL`, so multiple runs yield the same results.
- The maximum number of measurements is capped at 145. 
  - All sets of measurements start at `00:00:00`.
  - No set of measurements goes over `23:50:00`.
- Basic argument validation is provided ("foolproof").
- Error codes are returned by [WSAGetLastError()](https://learn.microsoft.com/en-us/windows/win32/api/winsock/nf-winsock-wsagetlasterror).
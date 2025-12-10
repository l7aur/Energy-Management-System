#include "Laptop.hpp"

std::list<Message> Laptop::getMessages(unsigned int day, unsigned int month, unsigned int year) const
{
    std::list<Message> messages{};

    for (unsigned int hour = 0; hour <= 17; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.000f
                }
            );

    // Charging starts
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.060f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.055f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.050f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.049f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.050f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.051f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.050f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.049f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.050f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.051f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.040f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.029f });

    for (unsigned int hour = 20; hour <= 23; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.000f
                }
            );

    messages.resize(numberOfReadings);
    return messages;
}

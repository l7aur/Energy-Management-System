#include "CoffeeMachine.hpp"

std::list<Message> CoffeeMachine::getMessages(unsigned int day, unsigned int month, unsigned int year) const
{
    std::list<Message> messages{};

    for (unsigned int hour = 0; hour <= 5; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.000
                }
            );

    // Brewing Cycle (High Consumption)
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.160f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.145f });

    // Keep Warm Cycle (Moderate/Intermittent Consumption)
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.025f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.020f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.025f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.020f });

    for (unsigned int hour = 7; hour <= 23; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.000
                }
            );

    messages.resize(numberOfReadings);
    return messages;
}
#include "Fridge.hpp"

std::list<Message> Fridge::getMessages(unsigned int day, unsigned int month, unsigned int year) const
{
    std::list<Message> messages{};

    for (unsigned int hour = 0; hour <= 23; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.05f + static_cast<float>((rand() % 2 ? -1 : 1) * (rand() % 10)) / 1000.0f
                }
            );

    messages.resize(numberOfReadings);
    return messages;
}

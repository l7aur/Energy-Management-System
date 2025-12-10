#include "TV.hpp"

std::list<Message> TV::getMessages(unsigned int day, unsigned int month, unsigned int year) const
{
    std::list<Message> messages{};

    // Off-Peak (00:00:00 - 05:50:00): Consumption = 0.000 kWh
    for (unsigned int hour = 0; hour <= 5; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.000f
                }
            );

    // Morning Usage (06:00:00 - 06:50:00): Consumption = ~0.080 kWh (News)
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.080f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.082f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.081f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.083f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.080f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 6, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.082f });

    // Off-Peak (07:00:00 - 16:50:00): Consumption = 0.000 kWh (No one home)
    for (unsigned int hour = 7; hour <= 16; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.000f
                }
            );
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 17, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.000f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 17, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.000f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 17, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.000f });

    // Evening Usage (17:30:00 - 22:50:00): Consumption = ~0.090 kWh (Peak viewing)
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 17, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.070f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 17, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.077f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 17, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.084f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 18, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.092f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.091f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.090f });

    // Off-Peak (23:00:00 - 23:50:00): Consumption returns to 0.000 kWh
    for (unsigned int minute = 0; minute <= 50; minute += 10)
        messages.emplace_back(
            Message{
                .timestamp = {.year = year, .month = month, .day = day, .hour = 23, .minute = minute, .second = 0},
                .deviceId = uid,
                .measuredValue = 0.000f
            }
        );

    messages.resize(numberOfReadings);
    return messages;
}

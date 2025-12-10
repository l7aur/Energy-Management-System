#include "PC.hpp"

std::list<Message> PC::getMessages(unsigned int day, unsigned int month, unsigned int year) const
{
    std::list<Message> messages{};
    // Off-Peak (00:00:00 - 18:50:00): Consumption at idle
    for (unsigned int hour = 0; hour <= 18; hour++)
        for (unsigned int minute = 0; minute <= 50; minute += 10)
            messages.emplace_back(
                Message{
                    .timestamp = {.year = year, .month = month, .day = day, .hour = hour, .minute = minute, .second = 0},
                    .deviceId = uid,
                    .measuredValue = 0.005f * (rand() * 100 % 37)
                }
            );

    // --- Extended Active Usage (19:00:00 - 23:50:00): Consumption = ~0.120 - 0.130 kWh ---
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.125f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.127f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.124f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.126f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.128f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 19, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.125f });

    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.130f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.129f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.127f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.124f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.126f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 20, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.128f });

    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.130f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.125f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.127f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.129f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.126f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 21, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.128f });

    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.130f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.125f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.127f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.129f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.126f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 22, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.128f });

    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 23, .minute = 0, .second = 0}, .deviceId = uid, .measuredValue = 0.130f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 23, .minute = 10, .second = 0}, .deviceId = uid, .measuredValue = 0.125f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 23, .minute = 20, .second = 0}, .deviceId = uid, .measuredValue = 0.127f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 23, .minute = 30, .second = 0}, .deviceId = uid, .measuredValue = 0.129f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 23, .minute = 40, .second = 0}, .deviceId = uid, .measuredValue = 0.126f });
    messages.emplace_back(Message{ .timestamp = {.year = year, .month = month, .day = day, .hour = 23, .minute = 50, .second = 0}, .deviceId = uid, .measuredValue = 0.128f });

    messages.resize(numberOfReadings);
    return messages;
}

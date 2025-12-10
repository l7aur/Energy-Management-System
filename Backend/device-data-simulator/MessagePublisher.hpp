#pragma once

#include "Message.hpp"
#include "MyConnectionHandler.hpp"

#include <list>
#include <memory>
#include <string_view>
#include <thread>

class MessagePublisher {
public:
	explicit MessagePublisher();
	~MessagePublisher();
	
	void publish(const std::list<Message>& messages) const;
	void end();
	void start();

private:
	std::unique_ptr<MyConnectionHandler> connectionHandler;
	std::unique_ptr<AMQP::Channel> channel;
	std::unique_ptr<AMQP::Connection> connection;
	std::thread socketReceiver;

	static constexpr std::string_view QUEUE_NAME{ "sensor-data-queue" };
	static constexpr std::string_view EXCHANGE_NAME{ "" };
	static constexpr std::string_view HOST{ "localhost" };
	static constexpr int PORT{ 5672 };
};
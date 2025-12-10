#include "MessagePublisher.hpp"

MessagePublisher::MessagePublisher()
{
	connectionHandler = std::make_unique<MyConnectionHandler>();
	connectionHandler->connectToRabbitMQ(HOST.data(), PORT);
	
	connection = std::make_unique<AMQP::Connection>(connectionHandler.get());
	channel = std::make_unique<AMQP::Channel>(connection.get());

	channel->declareQueue(QUEUE_NAME, AMQP::durable);
}

MessagePublisher::~MessagePublisher()
{
}

void MessagePublisher::publish(const std::list<Message>& messages) const
{
	while (!connection->ready())
		std::this_thread::sleep_for(std::chrono::milliseconds(500));
	for (const auto& msg : messages)
		if (!channel->publish(EXCHANGE_NAME, QUEUE_NAME, msg.getBody()))
			std::cout << "Failed to publish message!\n";
}

void MessagePublisher::end() 
{
	channel->close();
	connection->close();
	connectionHandler->close();

	if (socketReceiver.joinable())
		socketReceiver.join();
}

void MessagePublisher::start() 
{
	socketReceiver = std::thread([this] {
		char buffer[4096];
		while (true) {
			int bytes = recv(connectionHandler->getSocketFd(), buffer, sizeof(buffer), 0);
			if (bytes <= 0)
				break;
			connection->parse(buffer, bytes);
		}
	});
}
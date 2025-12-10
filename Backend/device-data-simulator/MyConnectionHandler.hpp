#pragma once

#define NOMINMAX
#include <amqpcpp.h>

#define WIN32_LEAN_AND_MEAN
#include <winsock2.h>

class MyConnectionHandler : public AMQP::ConnectionHandler {
public:
	explicit MyConnectionHandler();
	~MyConnectionHandler();

	bool connectToRabbitMQ(const char* host, int port);
	void close() const;
	SOCKET getSocketFd() const { return socketFd; }

private:
	SOCKET socketFd;

	void onData(AMQP::Connection* connection, const char* data, size_t size) override;
	void onError(AMQP::Connection* connection, const char* message) override;
	void onReady(AMQP::Connection* connection) override;
};
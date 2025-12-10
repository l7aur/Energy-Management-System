#include "MyConnectionHandler.hpp"

#include <ws2tcpip.h>
#include <windows.h>
#include <iostream>

#pragma comment(lib, "ws2_32.lib")

MyConnectionHandler::MyConnectionHandler()
	: socketFd{ INVALID_SOCKET }
{
	WSADATA wsa;
	if (WSAStartup(MAKEWORD(2, 2), &wsa))
		std::cout << "Windows WSA networking library 2.2 initialization failed!\n";
}

MyConnectionHandler::~MyConnectionHandler()
{
	if (socketFd != INVALID_SOCKET && closesocket(socketFd) == SOCKET_ERROR)
		std::cout << "Error while closing the socket: " << WSAGetLastError() << std::endl;
	WSACleanup();
}

bool MyConnectionHandler::connectToRabbitMQ(const char* host, int port)
{
	socketFd = socket(AF_INET, SOCK_STREAM, 0);
	if (socketFd == INVALID_SOCKET)
		return false;

	addrinfo hints{}, * res{ nullptr };
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_STREAM;

	if (getaddrinfo(host, std::to_string(port).c_str(), &hints, &res) != 0) {
		std::cout << "Failed to get address info! WSA error code: " << WSAGetLastError() << std::endl;
		return false;
	}

	bool isConnected = connect(socketFd, res->ai_addr, static_cast<int>(res->ai_addrlen)) != SOCKET_ERROR;
	freeaddrinfo(res);
	
	if (!isConnected)
	{
		std::cout << "Failed connecting to socket! WSA error code: " << WSAGetLastError() << std::endl;
		return false;
	}

	std::cout << "Successfully connected to " << host << ":" << port << std::endl;
	return true;
}

void MyConnectionHandler::close() const
{
	if (socketFd != INVALID_SOCKET && shutdown(socketFd, SD_RECEIVE) == SOCKET_ERROR)
		std::cout << "Error while shutdowning the socket: " << WSAGetLastError() << std::endl;
}	

void MyConnectionHandler::onData(AMQP::Connection* connection, const char* data, size_t size)
{
	int sentBytes = 0;
	while (sentBytes < size) {
		int msgByteSize = send(socketFd, data + sentBytes, static_cast<int>(size - sentBytes), 0);
		sentBytes += msgByteSize;
	}
}

void MyConnectionHandler::onError(AMQP::Connection* connection, const char* message)
{
	std::cout << "Registered RabbitMQ error: " << message << std::endl;
}

void MyConnectionHandler::onReady(AMQP::Connection* connection)
{
	std::cout << "Connection to RabbitMQ instance has been established!" << std::endl;
}

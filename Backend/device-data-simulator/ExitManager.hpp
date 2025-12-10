#pragma once

class ExitManager {
public:
	enum Code {
		SUCCESS,
		NOTHING_PUBLISHED,
		FAILURE,
		FAILED_TO_CONNECT
	};

	static int exit(const Code exitCode);
};
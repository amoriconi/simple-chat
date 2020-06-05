# simple-chat
Simple Java (1.8) chat server. Each connected client is managed by a specific thread. Every time a client sends a message terminated by the end of line character, it is propagated to all other connected clients. To try, just start the Main class and then with a Putty type client connect to port 10000 in Raw mode.

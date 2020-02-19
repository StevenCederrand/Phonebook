all:
	javac -d build/ src/*.java

server: all
	java -cp build/ Server

client: build
	java -cp build/ Client


clean:
	rm build/*.class

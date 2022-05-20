gen:
	 protoc ./proto/*.proto --proto_path=proto --go_out=. --go_opt=paths=import --go-grpc_out=. --go-grpc_opt=paths=import
clean:
	rm ./pb/simpleTutorial/*.go
run:
	go run main.gear
test:
	go test -cover -race ./...
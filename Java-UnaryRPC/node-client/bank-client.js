
const grpc = require("@grpc/grpc-js")
const loader = require("@grpc/proto-loader")

const packageDefinition = loader.loadSync("proto/bank-service.proto")
const protoDesc = grpc.loadPackageDefinition(packageDefinition)

const client = new protoDesc.BankService('localhost:8000', grpc.credentials.createInsecure())

client.getBalance({accountNumber: 4}, (err, balance) => {
	if (err) console.log("something wrong happened")

	console.log("Balance Received : RM " + balance.amount.value) // Int32Value: amount -> Int: amount.value
})

package service

import (
	"context"
	"github.com/stretchr/testify/require"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"net"
	"simpleTutorial/pb/simpleTutorial"
	"simpleTutorial/sample"
	"testing"
)

func TestClientCreateLaptop(t *testing.T) {
	t.Parallel()

	server, addr := startTestLaptopServer(t)
	client := newTestLaptopClient(t, addr)

	laptop := sample.NewLaptop()
	expectedId := laptop.Id

	req := &simpleTutorial.CreateLaptopRequest{
		Laptop: laptop,
	}

	res, err := client.CreateLaptop(context.Background(), req)
	require.NoError(t, err)
	require.Equal(t, expectedId, res.Id)

	// check that laptop is stored

	data, err := server.Store.Find(res.Id)
	require.NoError(t, err)
	require.NotNil(t, data)
	require.Equal(t, expectedId, data.Id)
}

func startTestLaptopServer(t *testing.T) (*LaptopServer, string) {
	server := NewLaptopServer(NewMemoryLaptopStore())

	grpcServer := grpc.NewServer()
	simpleTutorial.RegisterLaptopServiceServer(grpcServer, server)

	listener, err := net.Listen("tcp", ":0") // assign to any random available port
	require.NoError(t, err)

	go func() {
		err := grpcServer.Serve(listener)
		require.NoError(t, err)
	}()

	return server, listener.Addr().String()
}

func newTestLaptopClient(t *testing.T, addr string) simpleTutorial.LaptopServiceClient {

	connection, err := grpc.Dial(addr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	require.NoError(t, err)

	return simpleTutorial.NewLaptopServiceClient(connection)
}

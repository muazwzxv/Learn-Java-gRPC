package service

import (
	"context"
	"github.com/stretchr/testify/require"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"simpleTutorial/pb/simpleTutorial"
	"simpleTutorial/sample"
	"testing"
)

func TestServerLaptop(t *testing.T) {
	t.Parallel()

	laptopNoId := sample.NewLaptop()
	laptopNoId.Id = ""

	laptopInvalidId := sample.NewLaptop()
	laptopInvalidId.Id = "invalid uuid"

	laptopDuplicateId := sample.NewLaptop()
	storeDuplicateId := NewMemoryLaptopStore()
	err := storeDuplicateId.Save(laptopDuplicateId)
	require.Nil(t, err)

	testCases := []struct {
		name   string
		laptop *simpleTutorial.Laptop
		store  LaptopStore
		code   codes.Code
	}{
		{
			name:   "success create with id given",
			laptop: sample.NewLaptop(),
			store:  NewMemoryLaptopStore(),
			code:   codes.OK,
		},
		{
			name:   "success create with no id given",
			laptop: laptopNoId,
			store:  NewMemoryLaptopStore(),
			code:   codes.OK,
		},
		{
			name:   "failure invalid uuid",
			laptop: laptopInvalidId,
			store:  NewMemoryLaptopStore(),
			code:   codes.InvalidArgument,
		},
		{
			name:   "failure duplicate id",
			laptop: laptopDuplicateId,
			store:  storeDuplicateId,
			code:   codes.AlreadyExists,
		},
	}

	for i := range testCases {

		currentTest := testCases[i]
		t.Run(currentTest.name, func(t *testing.T) {
			t.Parallel()

			req := &simpleTutorial.CreateLaptopRequest{
				Laptop: currentTest.laptop,
			}

			testServer := NewLaptopServer(currentTest.store)
			res, err := testServer.CreateLaptop(context.Background(), req)

			if currentTest.code == codes.OK {
				require.NoError(t, err)
				require.NotNil(t, res)
				require.NotEmpty(t, res.Id)
				if len(currentTest.laptop.Id) > 0 {
					require.Equal(t, currentTest.laptop.Id, res.Id)
				}
			} else {
				require.Error(t, err)
				require.Nil(t, res)
				st, ok := status.FromError(err)
				require.True(t, ok)
				require.Equal(t, currentTest.code, st.Code())
			}
		})

	}
}

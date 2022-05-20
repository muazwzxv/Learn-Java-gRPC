package service

import (
	"context"
	"errors"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
	"log"
	"simpleTutorial/pb/simpleTutorial"
)

type LaptopServer struct {
	Store LaptopStore
}

func NewLaptopServer() *LaptopServer {
	return &LaptopServer{}
}

func (server *LaptopServer) CreateLaptop(ctx context.Context, req *simpleTutorial.CreateLaptopRequest) (*simpleTutorial.CreateLaptopResponse, error) {
	laptop := req.GetLaptop()
	log.Printf("received request: %v", laptop)

	if len(laptop.Id) > 0 {
		_, err := uuid.Parse(laptop.Id)
		if err != nil {
			return nil, status.Errorf(codes.InvalidArgument, "laptop ID is not a valid uuid: %v", err)
		}
	} else {
		id, err := uuid.NewRandom()
		if err != nil {
			return nil, status.Errorf(codes.Internal, "cannot generate new laptop uuid: ")
		}
		laptop.Id = id.String()
	}

	err := server.Store.Save(laptop)
	if err != nil {
		code := codes.Internal
		if errors.Is(err, ErrAlreadyExists) {
			code = codes.AlreadyExists
		}
		return nil, status.Errorf(code, "cannot save laptop: %w", err)
	}

	log.Printf("laptop saved successfully: %s", laptop)

	res := &simpleTutorial.CreateLaptopResponse{
		Id: laptop.Id,
	}
	return res, nil
}

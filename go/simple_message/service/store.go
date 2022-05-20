package service

import (
	"errors"
	"fmt"
	"github.com/jinzhu/copier"
	"simpleTutorial/pb/simpleTutorial"
	"sync"
)

var ErrAlreadyExists = errors.New("records already exist")

type LaptopStore interface {
	Save(laptop *simpleTutorial.Laptop) error
}

type InMemoryLaptopStore struct {
	mutex sync.RWMutex
	data  map[string]*simpleTutorial.Laptop
}

func NewMemoryLaptopStore() *InMemoryLaptopStore {
	return &InMemoryLaptopStore{
		data: make(map[string]*simpleTutorial.Laptop),
	}
}

func (store *InMemoryLaptopStore) Save(laptop *simpleTutorial.Laptop) error {
	store.mutex.Lock()
	defer store.mutex.Unlock()

	if store.data[laptop.Id] != nil {
		return ErrAlreadyExists
	}

	other := &simpleTutorial.Laptop{}
	err := copier.Copy(other, laptop)
	if err != nil {
		return fmt.Errorf("cannot copy laptop data: %w", err)
	}

	store.data[other.Id] = other
	return nil
}

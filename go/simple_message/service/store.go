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
	Find(id string) (*simpleTutorial.Laptop, error)
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

func (store *InMemoryLaptopStore) Find(id string) (*simpleTutorial.Laptop, error) {
	store.mutex.RLocker()
	defer store.mutex.RUnlock()

	laptop := store.data[id]
	if laptop == nil {
		return nil, fmt.Errorf("laptop not found id: %s", laptop.Id)
	}

	payload := &simpleTutorial.Laptop{}
	err := copier.Copy(payload, laptop)
	if err != nil {
		return nil, fmt.Errorf("cannot copy laptop data: %w", err)
	}

	return payload, nil
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

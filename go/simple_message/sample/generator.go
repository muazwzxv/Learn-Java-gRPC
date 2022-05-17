package sample

import (
	"google.golang.org/protobuf/types/known/timestamppb"
	"simpleTutorial/pb/simpleTutorial"
	"time"
)

func NewKeyboard() *simpleTutorial.Keyboard {
	return &simpleTutorial.Keyboard{
		Layout:  randomLayoutKeyboard(),
		Backlit: randomBool(),
	}
}

func NewCPU() *simpleTutorial.CPU {

	brand := randomCPUBrand()
	name := randomCPUName(brand)
	numCore := randomInt(2, 8)
	numThreads := randomInt(numCore, 12)

	minGhz := randomFloat64(2.0, 3.5)
	maxGhz := randomFloat64(minGhz, 5.0)

	return &simpleTutorial.CPU{
		Brand:      brand,
		Name:       name,
		NumCore:    uint32(numCore),
		NumThreads: uint32(numThreads),
		MinGhz:     minGhz,
		MaxGhz:     maxGhz,
	}
}

func NewGPU() *simpleTutorial.GPU {
	brand := randomGPUBrand()
	name := randomGPUName(brand)

	minGhz := randomFloat64(1.0, 1.5)
	maxGhz := randomFloat64(minGhz, 2.0)

	memory := &simpleTutorial.Memory{
		Value: uint64(randomInt(2, 6)),
		Unit:  simpleTutorial.Memory_GIGABYTE,
	}

	return &simpleTutorial.GPU{
		Brand:  brand,
		Name:   name,
		MinGhz: minGhz,
		MaxGhz: maxGhz,
		Memory: memory,
	}
}

func NewRAM() *simpleTutorial.Memory {
	return &simpleTutorial.Memory{
		Value: uint64(randomInt(2, 6)),
		Unit:  simpleTutorial.Memory_GIGABYTE,
	}
}

func NewSSD() *simpleTutorial.Storage {
	return &simpleTutorial.Storage{
		Driver: simpleTutorial.Storage_SSD,
		Memory: &simpleTutorial.Memory{
			Value: uint64(randomInt(120, 1024)),
			Unit:  simpleTutorial.Memory_GIGABYTE,
		},
	}
}

func NewHDD() *simpleTutorial.Storage {
	return &simpleTutorial.Storage{
		Driver: simpleTutorial.Storage_HDD,
		Memory: &simpleTutorial.Memory{
			Value: uint64(randomInt(1, 6)),
			Unit:  simpleTutorial.Memory_TERABYTE,
		},
	}
}

func NewScreen() *simpleTutorial.Screen {
	return &simpleTutorial.Screen{
		SizeInch:   randomFloat32(13, 17),
		Resolution: randomScreenResolution(),
		Panel:      randomScreenPanel(),
		MultiTouch: randomBool(),
	}
}

func NewLaptop() *simpleTutorial.Laptop {
	brand := randomLaptopBrand()
	name := randomLaptopName(brand)

	return &simpleTutorial.Laptop{
		Id:       randomID(),
		Screen:   NewScreen(),
		Brand:    brand,
		Name:     name,
		Cpu:      NewCPU(),
		Ram:      NewRAM(),
		Gpus:     []*simpleTutorial.GPU{NewGPU()},
		Storages: []*simpleTutorial.Storage{NewSSD(), NewHDD()},
		Keyboard: NewKeyboard(),
		Weight: &simpleTutorial.Laptop_WeightKg{
			WeightKg: randomFloat64(1.0, 3.0),
		},
		PriceUsd:    randomFloat64(1500, 3000),
		ReleaseYear: uint32(randomFloat64(2015, 2022)),
		UpdatedAt:   timestamppb.New(time.Now()),
	}
}

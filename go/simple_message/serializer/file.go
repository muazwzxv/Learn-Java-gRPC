package serializer

import (
	"fmt"
	"github.com/golang/protobuf/proto"
	"io/ioutil"
)

func WriteProtoToBinary(message proto.Message, filename string) error {
	data, err := proto.Marshal(message)
	if err != nil {
		return fmt.Errorf("cannot marshal proto message %w", err)
	}

	err = ioutil.WriteFile(filename, data, 0644)
	if err != nil {
		return fmt.Errorf("cannot write binary data to file %w", err)
	}

	return nil
}

func ReadProtoFromBinary(filename string, message proto.Message) error {
	data, err := ioutil.ReadFile(filename)
	if err != nil {
		return fmt.Errorf("cannot read binary %w", err)
	}

	err = proto.UnmarshalMerge(data, message)
	if err != nil {
		return fmt.Errorf("cannot unmarshal binary to proto %w", err)
	}

	return nil
}

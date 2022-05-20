package serializer

import (
	"fmt"
	"github.com/golang/protobuf/jsonpb"
	"github.com/golang/protobuf/proto"
	"io/ioutil"
)

func WriteProtoToBinary(msg proto.Message, filename string) error {
	data, err := proto.Marshal(msg)
	if err != nil {
		return fmt.Errorf("cannot marshal proto message %w", err)
	}

	err = ioutil.WriteFile(filename, data, 0644)
	if err != nil {
		return fmt.Errorf("cannot write binary data to file %w", err)
	}

	return nil
}

func WriteProtoToJson(msg proto.Message, filename string) error {
	data, err := ProtoToJson(msg)
	if err != nil {
		return fmt.Errorf("cannot serialize to json %w", err)
	}

	err = ioutil.WriteFile(filename, []byte(data), 0644)
	if err != nil {
		return fmt.Errorf("cannot write json to file %w", err)
	}

	return nil
}

func ProtoToJson(msg proto.Message) (string, error) {
	marshaller := jsonpb.Marshaler{
		EnumsAsInts:  false,
		EmitDefaults: true,
		Indent:       " ",
		OrigName:     true,
	}

	return marshaller.MarshalToString(msg)
}

func ReadProtoFromBinary(filename string, msg proto.Message) error {
	data, err := ioutil.ReadFile(filename)
	if err != nil {
		return fmt.Errorf("cannot read binary %w", err)
	}

	err = proto.UnmarshalMerge(data, msg)
	if err != nil {
		return fmt.Errorf("cannot unmarshal binary to proto %w", err)
	}

	return nil
}

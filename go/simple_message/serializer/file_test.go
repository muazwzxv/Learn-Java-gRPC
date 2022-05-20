package serializer

import (
	"github.com/stretchr/testify/require"
	"log"
	"simpleTutorial/pb/simpleTutorial"
	"simpleTutorial/sample"
	"testing"
)

func TestFileSerializer(t *testing.T) {
	t.Parallel()

	binaryFile := "../tmp/laptop.bin"
	laptop1 := sample.NewLaptop()

	err := WriteProtoToBinary(laptop1, binaryFile)
	require.NoError(t, err)
}

func TestReadFileSerializer(t *testing.T) {
	t.Parallel()

	binaryFile := "../tmp/laptop.bin"
	laptop := new(simpleTutorial.Laptop)

	err := ReadProtoFromBinary(binaryFile, laptop)
	require.NoError(t, err)
	require.NotNil(t, laptop)

	log.Printf("read from binary file: %v", laptop)
}

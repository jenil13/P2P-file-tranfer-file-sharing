# P2P-file-tranfer-file-sharing

Java P2P file sharing software similar to BitTorrent

Handshake message
The handshake consists of three parts: handshake header, zero bits, and peer ID. The length of the handshake message is 32 bytes. The handshake header is the 18-byte string ‘P2PFILESHARINGPROJ’, which is followed by 10-byte zero bits, which is followed by a 4-byte peer ID which is the integer representation of the peer ID.

Actual messages
After handshaking, each peer can send a stream of actual messages. An actual message consists of a 4-byte message length field, a 1-byte message type field, and a message payload with variable size.
The 4-byte message length specifies the message length in bytes. It does not include the length of the message length field itself. The 1-byte message type field specifies the type of the message.

Message Types

choke, unchoke, interested, not interested , have  , bitfield , request and Piece

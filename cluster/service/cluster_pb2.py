# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: cluster.proto
# Protobuf Python Version: 5.27.2
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    27,
    2,
    '',
    'cluster.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\rcluster.proto\x12\rcluster.proto\"@\n\x0e\x43lusterRequest\x12\x0c\n\x04path\x18\x01 \x01(\t\x12\x0c\n\x04lang\x18\x02 \x01(\t\x12\x12\n\nextensions\x18\x03 \x03(\t\";\n\x0f\x43lusterResponse\x12\x15\n\rclusterLabels\x18\x01 \x03(\x03\x12\x11\n\tfilePaths\x18\x02 \x03(\t2d\n\x0e\x43lusterService\x12R\n\x11PerformClustering\x12\x1d.cluster.proto.ClusterRequest\x1a\x1e.cluster.proto.ClusterResponseB1\n\x1c\x65\x64u.setokk.astrocluster.grpcB\x0f\x43lusterStubGrpcP\x01\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'cluster_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n\034edu.setokk.astrocluster.grpcB\017ClusterStubGrpcP\001'
  _globals['_CLUSTERREQUEST']._serialized_start=32
  _globals['_CLUSTERREQUEST']._serialized_end=96
  _globals['_CLUSTERRESPONSE']._serialized_start=98
  _globals['_CLUSTERRESPONSE']._serialized_end=157
  _globals['_CLUSTERSERVICE']._serialized_start=159
  _globals['_CLUSTERSERVICE']._serialized_end=259
# @@protoc_insertion_point(module_scope)

// Generated by the protocol buffer compiler.  DO NOT EDIT!
// NO CHECKED-IN PROTOBUF GENCODE
// source: cluster.proto
// Protobuf Java Version: 4.27.2

package edu.setokk.astrocluster.grpc;

/**
 * Protobuf type {@code cluster.proto.ClusterResponse}
 */
public final class ClusterResponse extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:cluster.proto.ClusterResponse)
    ClusterResponseOrBuilder {
private static final long serialVersionUID = 0L;
  static {
    com.google.protobuf.RuntimeVersion.validateProtobufGencodeVersion(
      com.google.protobuf.RuntimeVersion.RuntimeDomain.PUBLIC,
      /* major= */ 4,
      /* minor= */ 27,
      /* patch= */ 2,
      /* suffix= */ "",
      ClusterResponse.class.getName());
  }
  // Use ClusterResponse.newBuilder() to construct.
  private ClusterResponse(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private ClusterResponse() {
    clusterLabels_ = emptyLongList();
    filePaths_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return edu.setokk.astrocluster.grpc.ClusterStubGrpc.internal_static_cluster_proto_ClusterResponse_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return edu.setokk.astrocluster.grpc.ClusterStubGrpc.internal_static_cluster_proto_ClusterResponse_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            edu.setokk.astrocluster.grpc.ClusterResponse.class, edu.setokk.astrocluster.grpc.ClusterResponse.Builder.class);
  }

  public static final int CLUSTERLABELS_FIELD_NUMBER = 1;
  @SuppressWarnings("serial")
  private com.google.protobuf.Internal.LongList clusterLabels_ =
      emptyLongList();
  /**
   * <code>repeated int64 clusterLabels = 1;</code>
   * @return A list containing the clusterLabels.
   */
  @java.lang.Override
  public java.util.List<java.lang.Long>
      getClusterLabelsList() {
    return clusterLabels_;
  }
  /**
   * <code>repeated int64 clusterLabels = 1;</code>
   * @return The count of clusterLabels.
   */
  public int getClusterLabelsCount() {
    return clusterLabels_.size();
  }
  /**
   * <code>repeated int64 clusterLabels = 1;</code>
   * @param index The index of the element to return.
   * @return The clusterLabels at the given index.
   */
  public long getClusterLabels(int index) {
    return clusterLabels_.getLong(index);
  }
  private int clusterLabelsMemoizedSerializedSize = -1;

  public static final int FILEPATHS_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private com.google.protobuf.LazyStringArrayList filePaths_ =
      com.google.protobuf.LazyStringArrayList.emptyList();
  /**
   * <code>repeated string filePaths = 2;</code>
   * @return A list containing the filePaths.
   */
  public com.google.protobuf.ProtocolStringList
      getFilePathsList() {
    return filePaths_;
  }
  /**
   * <code>repeated string filePaths = 2;</code>
   * @return The count of filePaths.
   */
  public int getFilePathsCount() {
    return filePaths_.size();
  }
  /**
   * <code>repeated string filePaths = 2;</code>
   * @param index The index of the element to return.
   * @return The filePaths at the given index.
   */
  public java.lang.String getFilePaths(int index) {
    return filePaths_.get(index);
  }
  /**
   * <code>repeated string filePaths = 2;</code>
   * @param index The index of the value to return.
   * @return The bytes of the filePaths at the given index.
   */
  public com.google.protobuf.ByteString
      getFilePathsBytes(int index) {
    return filePaths_.getByteString(index);
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    if (getClusterLabelsList().size() > 0) {
      output.writeUInt32NoTag(10);
      output.writeUInt32NoTag(clusterLabelsMemoizedSerializedSize);
    }
    for (int i = 0; i < clusterLabels_.size(); i++) {
      output.writeInt64NoTag(clusterLabels_.getLong(i));
    }
    for (int i = 0; i < filePaths_.size(); i++) {
      com.google.protobuf.GeneratedMessage.writeString(output, 2, filePaths_.getRaw(i));
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    {
      int dataSize = 0;
      for (int i = 0; i < clusterLabels_.size(); i++) {
        dataSize += com.google.protobuf.CodedOutputStream
          .computeInt64SizeNoTag(clusterLabels_.getLong(i));
      }
      size += dataSize;
      if (!getClusterLabelsList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(dataSize);
      }
      clusterLabelsMemoizedSerializedSize = dataSize;
    }
    {
      int dataSize = 0;
      for (int i = 0; i < filePaths_.size(); i++) {
        dataSize += computeStringSizeNoTag(filePaths_.getRaw(i));
      }
      size += dataSize;
      size += 1 * getFilePathsList().size();
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof edu.setokk.astrocluster.grpc.ClusterResponse)) {
      return super.equals(obj);
    }
    edu.setokk.astrocluster.grpc.ClusterResponse other = (edu.setokk.astrocluster.grpc.ClusterResponse) obj;

    if (!getClusterLabelsList()
        .equals(other.getClusterLabelsList())) return false;
    if (!getFilePathsList()
        .equals(other.getFilePathsList())) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (getClusterLabelsCount() > 0) {
      hash = (37 * hash) + CLUSTERLABELS_FIELD_NUMBER;
      hash = (53 * hash) + getClusterLabelsList().hashCode();
    }
    if (getFilePathsCount() > 0) {
      hash = (37 * hash) + FILEPATHS_FIELD_NUMBER;
      hash = (53 * hash) + getFilePathsList().hashCode();
    }
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static edu.setokk.astrocluster.grpc.ClusterResponse parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static edu.setokk.astrocluster.grpc.ClusterResponse parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input);
  }
  public static edu.setokk.astrocluster.grpc.ClusterResponse parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessage
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(edu.setokk.astrocluster.grpc.ClusterResponse prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code cluster.proto.ClusterResponse}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:cluster.proto.ClusterResponse)
      edu.setokk.astrocluster.grpc.ClusterResponseOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return edu.setokk.astrocluster.grpc.ClusterStubGrpc.internal_static_cluster_proto_ClusterResponse_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return edu.setokk.astrocluster.grpc.ClusterStubGrpc.internal_static_cluster_proto_ClusterResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              edu.setokk.astrocluster.grpc.ClusterResponse.class, edu.setokk.astrocluster.grpc.ClusterResponse.Builder.class);
    }

    // Construct using edu.setokk.astrocluster.grpc.ClusterResponse.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      clusterLabels_ = emptyLongList();
      filePaths_ =
          com.google.protobuf.LazyStringArrayList.emptyList();
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return edu.setokk.astrocluster.grpc.ClusterStubGrpc.internal_static_cluster_proto_ClusterResponse_descriptor;
    }

    @java.lang.Override
    public edu.setokk.astrocluster.grpc.ClusterResponse getDefaultInstanceForType() {
      return edu.setokk.astrocluster.grpc.ClusterResponse.getDefaultInstance();
    }

    @java.lang.Override
    public edu.setokk.astrocluster.grpc.ClusterResponse build() {
      edu.setokk.astrocluster.grpc.ClusterResponse result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public edu.setokk.astrocluster.grpc.ClusterResponse buildPartial() {
      edu.setokk.astrocluster.grpc.ClusterResponse result = new edu.setokk.astrocluster.grpc.ClusterResponse(this);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartial0(edu.setokk.astrocluster.grpc.ClusterResponse result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        clusterLabels_.makeImmutable();
        result.clusterLabels_ = clusterLabels_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        filePaths_.makeImmutable();
        result.filePaths_ = filePaths_;
      }
    }

    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof edu.setokk.astrocluster.grpc.ClusterResponse) {
        return mergeFrom((edu.setokk.astrocluster.grpc.ClusterResponse)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(edu.setokk.astrocluster.grpc.ClusterResponse other) {
      if (other == edu.setokk.astrocluster.grpc.ClusterResponse.getDefaultInstance()) return this;
      if (!other.clusterLabels_.isEmpty()) {
        if (clusterLabels_.isEmpty()) {
          clusterLabels_ = other.clusterLabels_;
          clusterLabels_.makeImmutable();
          bitField0_ |= 0x00000001;
        } else {
          ensureClusterLabelsIsMutable();
          clusterLabels_.addAll(other.clusterLabels_);
        }
        onChanged();
      }
      if (!other.filePaths_.isEmpty()) {
        if (filePaths_.isEmpty()) {
          filePaths_ = other.filePaths_;
          bitField0_ |= 0x00000002;
        } else {
          ensureFilePathsIsMutable();
          filePaths_.addAll(other.filePaths_);
        }
        onChanged();
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              long v = input.readInt64();
              ensureClusterLabelsIsMutable();
              clusterLabels_.addLong(v);
              break;
            } // case 8
            case 10: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              ensureClusterLabelsIsMutable();
              while (input.getBytesUntilLimit() > 0) {
                clusterLabels_.addLong(input.readInt64());
              }
              input.popLimit(limit);
              break;
            } // case 10
            case 18: {
              java.lang.String s = input.readStringRequireUtf8();
              ensureFilePathsIsMutable();
              filePaths_.add(s);
              break;
            } // case 18
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private com.google.protobuf.Internal.LongList clusterLabels_ = emptyLongList();
    private void ensureClusterLabelsIsMutable() {
      if (!clusterLabels_.isModifiable()) {
        clusterLabels_ = makeMutableCopy(clusterLabels_);
      }
      bitField0_ |= 0x00000001;
    }
    /**
     * <code>repeated int64 clusterLabels = 1;</code>
     * @return A list containing the clusterLabels.
     */
    public java.util.List<java.lang.Long>
        getClusterLabelsList() {
      clusterLabels_.makeImmutable();
      return clusterLabels_;
    }
    /**
     * <code>repeated int64 clusterLabels = 1;</code>
     * @return The count of clusterLabels.
     */
    public int getClusterLabelsCount() {
      return clusterLabels_.size();
    }
    /**
     * <code>repeated int64 clusterLabels = 1;</code>
     * @param index The index of the element to return.
     * @return The clusterLabels at the given index.
     */
    public long getClusterLabels(int index) {
      return clusterLabels_.getLong(index);
    }
    /**
     * <code>repeated int64 clusterLabels = 1;</code>
     * @param index The index to set the value at.
     * @param value The clusterLabels to set.
     * @return This builder for chaining.
     */
    public Builder setClusterLabels(
        int index, long value) {

      ensureClusterLabelsIsMutable();
      clusterLabels_.setLong(index, value);
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 clusterLabels = 1;</code>
     * @param value The clusterLabels to add.
     * @return This builder for chaining.
     */
    public Builder addClusterLabels(long value) {

      ensureClusterLabelsIsMutable();
      clusterLabels_.addLong(value);
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 clusterLabels = 1;</code>
     * @param values The clusterLabels to add.
     * @return This builder for chaining.
     */
    public Builder addAllClusterLabels(
        java.lang.Iterable<? extends java.lang.Long> values) {
      ensureClusterLabelsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, clusterLabels_);
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>repeated int64 clusterLabels = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearClusterLabels() {
      clusterLabels_ = emptyLongList();
      bitField0_ = (bitField0_ & ~0x00000001);
      onChanged();
      return this;
    }

    private com.google.protobuf.LazyStringArrayList filePaths_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
    private void ensureFilePathsIsMutable() {
      if (!filePaths_.isModifiable()) {
        filePaths_ = new com.google.protobuf.LazyStringArrayList(filePaths_);
      }
      bitField0_ |= 0x00000002;
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @return A list containing the filePaths.
     */
    public com.google.protobuf.ProtocolStringList
        getFilePathsList() {
      filePaths_.makeImmutable();
      return filePaths_;
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @return The count of filePaths.
     */
    public int getFilePathsCount() {
      return filePaths_.size();
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @param index The index of the element to return.
     * @return The filePaths at the given index.
     */
    public java.lang.String getFilePaths(int index) {
      return filePaths_.get(index);
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @param index The index of the value to return.
     * @return The bytes of the filePaths at the given index.
     */
    public com.google.protobuf.ByteString
        getFilePathsBytes(int index) {
      return filePaths_.getByteString(index);
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @param index The index to set the value at.
     * @param value The filePaths to set.
     * @return This builder for chaining.
     */
    public Builder setFilePaths(
        int index, java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureFilePathsIsMutable();
      filePaths_.set(index, value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @param value The filePaths to add.
     * @return This builder for chaining.
     */
    public Builder addFilePaths(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      ensureFilePathsIsMutable();
      filePaths_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @param values The filePaths to add.
     * @return This builder for chaining.
     */
    public Builder addAllFilePaths(
        java.lang.Iterable<java.lang.String> values) {
      ensureFilePathsIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, filePaths_);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearFilePaths() {
      filePaths_ =
        com.google.protobuf.LazyStringArrayList.emptyList();
      bitField0_ = (bitField0_ & ~0x00000002);;
      onChanged();
      return this;
    }
    /**
     * <code>repeated string filePaths = 2;</code>
     * @param value The bytes of the filePaths to add.
     * @return This builder for chaining.
     */
    public Builder addFilePathsBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      ensureFilePathsIsMutable();
      filePaths_.add(value);
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    // @@protoc_insertion_point(builder_scope:cluster.proto.ClusterResponse)
  }

  // @@protoc_insertion_point(class_scope:cluster.proto.ClusterResponse)
  private static final edu.setokk.astrocluster.grpc.ClusterResponse DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new edu.setokk.astrocluster.grpc.ClusterResponse();
  }

  public static edu.setokk.astrocluster.grpc.ClusterResponse getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<ClusterResponse>
      PARSER = new com.google.protobuf.AbstractParser<ClusterResponse>() {
    @java.lang.Override
    public ClusterResponse parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<ClusterResponse> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<ClusterResponse> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public edu.setokk.astrocluster.grpc.ClusterResponse getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}


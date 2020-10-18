package com.github.wutut.grpc;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
    public static void main(String[] args) {
        ManagedChannel channel = null;
        try {
            // channel = ManagedChannelBuilder.forAddress("192.168.31.96",
            // 50051).usePlaintext().build();
            channel = ManagedChannelBuilder.forAddress("127.0.0.1", 50051).usePlaintext().build();
            AudioInfoGrpc.AudioInfoBlockingStub stub = AudioInfoGrpc.newBlockingStub(channel);
            for (int i = 0; i < 10; i++) {
                AudioInfoReply audioInfoReply = stub
                        .transinfo(AudioInfoRequest.newBuilder().setName("one is :").build());
                System.out.println(audioInfoReply.getTimestamp());
                System.out.println(audioInfoReply.getDetected());
                System.out.println("mic count" + audioInfoReply.getMicinfosCount());
                List<AudioInfoReply.MicInfo> micInfos = audioInfoReply.getMicinfosList();
                for (AudioInfoReply.MicInfo x : micInfos) {
                    System.out.println("name Mic " + x.getMicname() + "probability is " + x.getProbability());
                }
                TimeUnit.SECONDS.sleep(2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

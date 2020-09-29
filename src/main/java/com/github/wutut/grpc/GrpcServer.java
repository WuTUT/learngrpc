package com.github.wutut.grpc;

import io.grpc.stub.StreamObserver;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.wutut.grpc.AudioInfoGrpc.AudioInfoImplBase;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {
    public static void main(String[] args) {
        try {

            int port = 50051;
            final Server server = ServerBuilder.forPort(port).addService(new AudioServiceImpl()).build().start();
            System.out.println("Server started, listening on " + port);
            server.awaitTermination();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class AudioServiceImpl extends AudioInfoImplBase {
    void createInfo(AudioInfoReply.Builder audioInfoBuilder) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datestr = simpleDateFormat.format(date);
        audioInfoBuilder.setTimestamp(datestr);
        audioInfoBuilder.setDetected(true);
        for (int i = 0; i < 8; i++) {
            AudioInfoReply.MicInfo.Builder micInfobBuilder = AudioInfoReply.MicInfo.newBuilder();
            micInfobBuilder.setMicname("micro num " + i);
            micInfobBuilder.setProbability(0.05 * i);
            audioInfoBuilder.addMicinfos(micInfobBuilder);
        }

    }

    @Override
    public void transInfo(AudioInfoRequest request, StreamObserver<AudioInfoReply> responseObserver) {
        AudioInfoReply.Builder audioInfoBuilder = AudioInfoReply.newBuilder();
        createInfo(audioInfoBuilder);
        AudioInfoReply reply = audioInfoBuilder.build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
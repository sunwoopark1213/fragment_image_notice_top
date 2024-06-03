package com.example.fragment_image_notice_top;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

public class FragmentOne extends Fragment {

    private static final String CHANNEL_ID = "default";
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 기능 1: 프래그먼트 레이아웃 설정 및 뷰 생성
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        // 기능 2: 이미지 클릭 리스너 설정 (이미지 클릭 시 알림 발생 조건 체크)
        ImageView imageView = view.findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // 기능 4: 알림 권한 요청
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_PERMISSION_REQUEST_CODE);
                    } else {
                        // 기능 5: 알림 생성 및 표시
                        showNotification();
                    }
                } else {
                    // 기능 5: 알림 생성 및 표시
                    showNotification();
                }
            }
        });

        // 기능 3: 알림 채널 생성
        createNotificationChannel();

        return view;
    }

    // 기능 5: 알림 생성 및 표시
    private void showNotification() {
        Context context = getActivity();
        if (context != null) {
            Intent intent = new Intent(context, SecondActivity.class);
            // 기능 7: 알림 클릭 시 다른 액티비티로 전환
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // 기능 8: 알림에 큰 텍스트 스타일 추가
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle()
                    .bigText("알림 내용");

            // 기능 9: 알림에 대한 동작 추가 (예: 알림에서 직접 응답)
            Intent actionIntent = new Intent(context, SecondActivity.class);
            PendingIntent actionPendingIntent = PendingIntent.getActivity(context, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.notification_icon, "응답", actionPendingIntent).build();

            // 기능 10: 알림에 메시지 스타일 추가
            NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("사용자 이름")
                    .setConversationTitle("대화 제목")
                    .addMessage("첫 번째 메시지", System.currentTimeMillis(), "보낸 사람")
                    .addMessage("두 번째 메시지", System.currentTimeMillis(), "보낸 사람");

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("알림 제목")
                    .setContentText("알림 내용")
                    .setStyle(messagingStyle) // 메시지 스타일 추가
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .addAction(action)  // 액션 추가
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            notificationManagerCompat.notify(1, builder.build());
        }
    }

    // 기능 3: 알림 채널 생성
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Default Channel";
            String description = "Channel for default notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // 기능 6: 권한 요청 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되면 알림을 표시합니다.
                showNotification();
            }
        }
    }
}

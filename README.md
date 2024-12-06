# スレッドプール管理デモアプリケーション

## 概要
このアプリケーションは、Spring Bootにおける効率的なスレッドプール管理を実証するためのデモです。
複数の外部APIを並列で呼び出す際のベストプラクティスを示しています。

### 主な特徴
1. **共有スレッドプールの実装**
   - アプリケーション全体で1つのスレッドプールを共有
   - システムのリソースに応じた適切なスレッド数の自動設定
   - スレッドの再利用による効率的なリソース管理

2. **並列処理の実装例**
   - 外部API呼び出しのシミュレーション
   - タイムアウト処理の実装
   - スレッドプールの状態監視機能

3. **スレッド管理の最適化**
   - 同時実行数の制御
   - メモリ使用量の効率化
   - スレッド数の爆発的増加の防止

## システム構成

### 主要コンポーネント
1. **ThreadPoolConfig**
   - スレッドプールの設定と管理
   - ExecutorServiceのBean定義
   - スレッド数の最適化設定

2. **ExternalApiService**
   - 外部API呼び出しのシミュレーション
   - 並列処理の実装
   - タイムアウト処理の管理

3. **ThreadDemoController**
   - RESTエンドポイントの提供
   - 並列処理のテスト機能
   - スレッドプール状態の監視

### APIエンドポイント
- `GET /api/parallel-calls?numberOfCalls={n}`
  - n個の並列API呼び出しを実行
  - レスポンスに処理結果とスレッドプール状態を含む

- `GET /api/thread-pool-stats`
  - 現在のスレッドプール状態を取得

## 実行方法

### 前提条件
- Java 8以上
- Maven
- PowerShell（テストスクリプト実行用）

### セットアップと実行手順

1. **アプリケーションの起動**
   ```powershell
   mvn spring-boot:run
   ```
   - アプリケーションが起動し、8080ポートでリッスンを開始
   - "Started ThreadPoolDemoApplication"メッセージを確認

2. **テストスクリプトの実行**
   別のターミナルウィンドウで以下を実行：
   ```powershell
   .\test-thread-pool.ps1
   ```

### テストシナリオ
テストスクリプトは以下のシナリオを実行：
1. 5つの並列呼び出し
2. 10つの並列呼び出し
3. 20つの並列呼び出し
4. 複数クライアントからの同時アクセスシミュレーション

## 設計上の考慮点

### スレッドプール設計
- システムのプロセッサ数に基づいて最適なスレッド数を設定
- スレッドの再利用によるリソース効率の最適化
- アプリケーション全体での共有による管理の一元化

### エラー処理
- タイムアウト処理の実装
- 例外のハンドリング
- エラー時のリソース解放

### モニタリング
- スレッドプールの状態監視
- 実行統計の収集
- Actuatorエンドポイントによる健全性チェック

## 運用上の注意点
- 本番環境での使用時は、システムの特性に応じてスレッド数を適切に調整すること
- 大量のリクエストが予想される場合は、負荷テストを実施すること
- メモリ使用量を定期的に監視すること
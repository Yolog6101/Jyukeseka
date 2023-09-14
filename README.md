# ジュケセカ

## 概要
受験生を対象とした勉強・生活管理アプリケーションです。Android Pixel3a(1080×2220)に対応しています。マネージャ担当1人、プログラム担当4人で開発を行いました。

本アプリケーションには以下の機能があります。
- 起動時に本人認証を行う機能
    - 最初の起動時に6文字以上のパスワードを設定し、２回目以降は設定したパスワードを入力しないとアプリケーションの以下の機能を使用できないようにしています。
    - 設定したパスワードは本人認証後、設定から変更することもできます。

- 各教科の長期目標・今日の目標を設定する機能
    - 各教科(現代文・古文・漢文・英語・数学・化学・物理・生物・地学・日本史・世界史・地理・政治経済・倫理・その他の全15種)で永続的な長期目標を最大6つ、今日の目標を最大2つまで設定することができます。

- 各教科の勉強時間をタイマーを用いて記録・グラフ化する機能
    - 各教科で勉強時間をタイマーを用いて計測し、記録することができます。
    - 計測・記録した勉強時間は各教科ごとにグラフで見ることができます。

- 模試の成績(点数・偏差値)を記録・確認できる機能
    - 模試の名前と各教科の点数・偏差値を入力し、いつでも確認することができます。

- 健康記録として、運動時間・睡眠時間・体重を記録、グラフ化する機能
    - 各項目において時間や体重を入力することで記録することができます。
    - 記録したデータは各項目ごとにグラフで見ることができます。

- 勉強時間や健康記録の入力後、SNS等で共有できる機能
    - 入力後、テキストを記入して「共有する」ボタンを押すと、自動で共有方法が並んだポップアップを表示します。

- 勉強時間・体重において、記録時に先週との比較を自動で行い、ポップアップで表示する機能
    - ポップアップは設定からON/OFFを切り替えることができます。

## スクリーンショット
- 本人認証機能
    - 初回起動時
    <img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/743041de-b1fb-4cc2-ae45-6520572b3916" width="300px">

    - 2回目以降
    <img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/229fcdf7-f15c-493d-b9a5-8989a71a0ac9" width="300px">


- 目標設定機能
    <img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/28315058-1312-43b3-8d35-7e83fc225349" width="300px">

- 勉強時間を記録するタイマー機能
<img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/327049ad-dc69-4d56-904d-e40d40949338" width="300px">

- 模試の成績の記録機能
<img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/03457c6a-d003-42f7-9f66-9240ca9ff0c2" width="300px">

- 健康記録のグラフ化機能
<img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/64ecb9df-0dc6-43f6-8c85-6f4a1806abc6" width="300px">

- SNS等への共有機能
<img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/775d7961-c8c2-48f0-beea-402fe75789de" width="300px">

- ポップアップ機能
<img src="https://github.com/Yolog6101/Jyukeseka/assets/72485319/279090ec-5e96-4f09-947e-986baa1c5f88" width="300px">

## アピールポイント
　このアプリケーションでは勉強時間や模試の記録のほかに、受験生がおろそかにしがちな睡眠や運動などを記録して生活管理も行うことができるようにしています。また、各教科では永続的な目標だけでなく、その日の目標を別に設けることを可能にすることで、
受験生がより具体的な目的意識を持って勉強することができるようにしています。さらに記録した勉強時間や健康記録をグラフで確認可能にするだけでなく、総勉強時間や体重での先週との比較をポップアップ表示することで、たくさん勉強した・健康的な暮らしをしているという達成感や、勉強をしていない・不健康な暮らしをしているという危機感などを感じてもらうことも狙いとしています。

　実装・技術面としては、まず受験生でも簡単に使えるようにシンプルかつ統一感のあるUIを実現しました。また、目標や各勉強・健康記録はDATAファイルでスマートフォン内部に保存し、必要時のみ取り出し・上書きすることができるようにしています。さらに勉強時間や体重におけるポップアップではAndroid端末にデフォルトで存在するトーストではなく、独自のポップアップを作成・利用することで文字数の多い勉強時間や体重の先週との比較情報も難なく表示することができるようにしました。

## 開発言語・環境
- Android Studio
- Java
- PowerPoint(素材作成)
- MediBang Paint Pro(素材作成)

## マニュアル
　マニュアルは「ジュケセカ_マニュアル.pdf」をご覧ください。このプログラムはAndroid Studioで開発しているため、搭載されているエミュレータでも動かすことができます。また、デバイスにデプロイする場合もAndroid Studioでこのプログラムを開き、規定の方法に則ってください。
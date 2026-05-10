# PROJECT INFORMATION


---

### 1. Titik Masuk (Entry Point)

* **Class `Main`**
* **Kegunaan & Tugas:** Menjadi file utama tempat program Java pertama kali dieksekusi.
* **Fungsi:** Berisi method statis `main(String[] args)`.
* **Hubungan:** Membuat dan memanggil objek (instansiasi) `MainController` untuk memulai aplikasi.



---

### 2. Antarmuka / Kontrak (Interfaces)

* **Interface `IMonitorable`**
* **Kegunaan:** Menjamin bahwa perangkat keras dapat dipantau.
* **Fungsi:** Mewajibkan adanya fungsi seperti `getTemperature()`, `getPowerDraw()`, dan `getStatus()`.
* **Hubungan:** Di-implementasikan (*implements*) oleh `HardwareEquipment`.


* **Interface `IManageable`**
* **Kegunaan:** Menjamin perangkat keras bisa dikendalikan daya/sistemnya.
* **Fungsi:** Mewajibkan adanya fungsi `turnOn()`, `turnOff()`, dan `restart()`.
* **Hubungan:** Di-implementasikan (*implements*) oleh `HardwareEquipment`.


* **Interface `ICRUD<T>`**
* **Kegunaan:** Menstandarisasi operasi database secara dinamis menggunakan *Generics* (`<T>`).
* **Fungsi:** Memuat fungsi standar: Create, Read, Update, Delete.
* **Hubungan:** Di-implementasikan oleh kelas Data Access Object (seperti `ServerDAO`).



---

### 3. Model Domain (Pusat Logika OOP & Hierarki)

* **Abstract Class `HardwareEquipment'**
* **Kegunaan:** Cetak biru tertinggi untuk semua benda fisik (hardware) di data center.
* **Tugas & Fungsi:** Mengenkapsulasi ID Aset, nama model, ukuran U, dan status dasar.
* **Hubungan:** Mengimplementasikan interface `IMonitorable` & `IManageable`. Bertindak sebagai *Parent Class* utama (di-*extends* oleh `ComputingDevice`).


* **Abstract Class `ComputingDevice`**
* **Kegunaan:** Mengkhususkan hardware yang memiliki kemampuan komputasi.
* **Tugas & Fungsi:** Menambahkan atribut `cpuCores` dan `totalRamGB`.
* **Hubungan:** Mewarisi (*extends*) dari `HardwareEquipment` dan mewariskan sifatnya ke `Server`.


* **Class `Server`**
* **Kegunaan:** Objek riil yang mewakili mesin server fisik.
* **Tugas & Fungsi:** Menyimpan OS, Utilisasi CPU, Utilisasi RAM, Total Storage, dan Used Storage. **Fungsi Khusus:** Melakukan *Method Overriding* pada `setStatus(String status)`. Jika status diubah menjadi "Offline" atau "Maintenance", fungsi ini secara otomatis memaksa `cpuUtilization` dan `ramUtilization` menjadi 0.
* **Hubungan:** Mewarisi (*extends*) dari `ComputingDevice`.


* **Class `GridLocation`**
* **Kegunaan:** Mewakili titik koordinat fisik di lantai data center.
* **Tugas & Fungsi:** Menyimpan koordinat X, Y, dan nama zona (misal: "Zona A").
* **Hubungan:** Memiliki relasi *Komposisi* (keterikatan mutlak) dengan `ServerRack`.


* **Class `ServerRack`**
* **Kegunaan:** Mewakili lemari rak besi.
* **Tugas & Fungsi:** Mengelola kapasitas ruang/slot (U). Menggunakan keyword `synchronized` pada method `addEquipment()` dan `removeEquipment()` agar tidak terjadi tabrakan data (error) saat Multithreading beroperasi (misal: menghapus server saat proses input massal berjalan).
* **Hubungan:** Memiliki relasi *Agregasi* (menampung) dengan `HardwareEquipment` dan *Komposisi* dengan `GridLocation`.


* **Class `DataCenterRoom`**
* **Kegunaan:** Mewakili ruangan yang berisi banyak rak.
* **Tugas & Fungsi:** Mencari lokasi rak dan mengkalkulasi kumpulan rak yang ada.
* **Hubungan:** Memiliki relasi *Agregasi* terhadap sekumpulan `ServerRack`.



---

### 4. Database Layer (Akses Data / DAO)

* **Class `DatabaseConnection`**
* **Kegunaan:** Jembatan komunikasi ke database MySQL.
* **Tugas & Fungsi:** Menggunakan pola *Singleton* agar aplikasi hanya membuka satu pintu koneksi yang stabil, mencegah pemborosan memori.


* **Class `ServerDAO`**
* **Kegunaan:** Mengurus transaksi data khusus untuk Server.
* **Tugas & Fungsi:** Mengeksekusi query SQL (INSERT, SELECT, UPDATE, DELETE). Memiliki fungsi tambahan `moveServerRack()` untuk memindahkan data server dari satu rak ke rak lain di MySQL.
* **Hubungan:** Mengimplementasikan `ICRUD<Server>` dan dipanggil oleh Controller & Thread.



---

### 5. Multithreading (Pemrosesan Latar Belakang)

* **Class `BatchOperationThread`**
* **Kegunaan:** Mengelola eksekusi tugas berat (Create, Update, Delete, Move massal) agar aplikasi (GUI) tidak macet/freeze.
* **Tugas & Fungsi:** Memproses sekumpulan `List<Server>` satu per satu ke database (`ServerDAO`). Secara periodik menyuruh `BatchProgressDialog` untuk menggerakkan bar loading (menggunakan `SwingUtilities`).
* **Hubungan:** Dipicu/dibuat oleh `RackController`.


* **Class `MonitorThread`**
* **Kegunaan:** Simulasi pantauan mesin server.
* **Tugas & Fungsi:** Berjalan terus-menerus di *background* untuk me-refresh data utilisasi atau menarik data metrik ke GUI.
* **Hubungan:** Dipicu oleh `ServerController`.



---

### 6. Controller (Pengendali Logika MVC)

* **Class `MainController`**
* **Tugas & Fungsi:** Memuat data awal saat aplikasi dibuka dan mengontrol pembuatan grid layout (denah rak).
* **Hubungan:** Mengendalikan `MainDashboardView` dan memicu `RackController` saat sebuah rak diklik.


* **Class `RackController`**
* **Tugas & Fungsi:** Menghitung sisa kapasitas (U) rak saat ini, memvalidasi form multi-input, dan menyuruh `BatchOperationThread` bekerja jika form disubmit.
* **Hubungan:** Mengendalikan `RackDetailView` dan `BatchServerFormView`.


* **Class `ServerController`**
* **Tugas & Fungsi:** Mengambil data tunggal server. Memiliki fungsi khusus `updateManualUtilization(cpu, ram)` untuk menangkap angka yang diketik oleh user, lalu memperbaruinya ke objek model dan database.
* **Hubungan:** Mengendalikan `ServerDetailView`.



---

### 7. View (Antarmuka Pengguna GUI / Java Swing)

* **Class `MainDashboardView`**
* **Tugas & Fungsi:** Frame/Jendela paling awal yang menampilkan peta visual (grid) dari seluruh rak di data center.


* **Class `RackDetailView`**
* **Tugas & Fungsi:** Kotak dialog yang memvisualisasikan isi dalam satu rak (berapa slot yang dipakai, berapa yang kosong). Terdapat tombol aksi seperti "Batch Add" atau "Batch Move".


* **Class `BatchServerFormView`**
* **Tugas & Fungsi:** Layar formulir dinamis tempat user bisa menambahkan banyak kolom server secara bersamaan. Form akan terkunci (disable) otomatis jika input yang ditambah sudah melebihi kapasitas maksimum sisa rak.


* **Class `BatchProgressDialog`**
* **Tugas & Fungsi:** Jendela *loading bar* yang muncul saat Multithreading memproses operasi massal.


* **Class `ServerDetailView`**
* **Tugas & Fungsi:** Panel khusus untuk menampilkan spesifikasi rinci satu server. Terdapat 3 *Progress Bar* (untuk Utilisasi CPU, Utilisasi RAM, dan Kapasitas Storage). Terdapat juga kolom *Text Field* dan tombol agar user bisa mengetik dan mengubah nilai utilisasi secara manual.
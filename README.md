## Tugas Kecil 1 Strategi Algoritma (IF2211)

Tugas Kecil 1 untuk mata kuliah Strategi Algoritma (IF2211) adalah IQ Puzzler Pro solver. Diberikan papan IQ Puzzler Pro dengan beberapa potongannya, digunakan algoritma brute force, yaitu dengan mencoba semua kemungkinan dari posisi dan oritentasi balok pada papan hingga mendapatkan solusi (jika ada).

### Struktur Program

Digunakan bahasa Java. Maka, diperlukan compiler Java dengan JDK (Java Development Kit) perlu terinstall. Jika sudah ada, berikut langkah-langkah untuk menjalankan program.

1. Clone repository.

```python
git clone https://github.com/adielrum/Tucil1_10123053
```

2. Navigate to `src` directory.

```python
cd Tucil1_10123004/src
```

3. Compile `App.java`.

```python
javac Main.java
```

4. Execute `App`.

```python
java Main
```

- `src`: folder untuk source code
- `bin`: folder untuk executables
- `test`: folder untuk file test case dan solusinya dalam `.txt`
- `doc`: folder untuk laporan dalam `.pdf`

### Cara Kerja Program

Solver ini berupa program CLI. Berikut cara program menerima input puzzle.

1. Sesuai prompt, isi absolute atau relative path dari file `.txt` yang sesuai.
2. Jika `.txt` sudah berada di folder `test`, cukup tulis nama filenya seperti `nama.txt`.
3. Perhatikan agar `.txt` sudah memiliki format seperti pada spesifikasi Tucil.

Berikut cara program menghasilkan output.

1. Puzzle akan diselesaikan dengan algoritma brute-force (akan diperlukan waktu tergantung kesulitan puzzle).
2. Jika ada solusi, program akan menghasilkan output berupa papan dan letak setiap blok pada papan yang sudah diwarnakan secara unik.
3. Ditampilkan juga waktu yang dimakan dan kasus yang ditelusuri oleh algoritma.
4. Dengan mengikuti prompt terakhir, output tersebut dapat disimpan dalam suatu `.txt` pada folder `test`. File solusi akan memiliki nama sama dengan file input tetapi dengan sufiks `_Solution.txt`.

Diberikan input seperti berikut.

```python
5 5 7
DEFAULT
A
AA
B
BB
C
CC
D
DD
EE
EE
E
FF
FF
F
GGG
```

Dengan output seperti berikut.

```python
Solution found:
A G G G C 
A A B C C 
E E B B F 
E E D F F 
E D D F F 
Total iterations: 118374
Execution time: 1805 ms
```

### Authors

Adiel Rum (10123004).
# CrateSystem

Minecraft **1.21.4** (Paper/Spigot) için, CrazyCrates / ExcellentCrates tarzında,
sıfırdan yazılmış, tamamen config edilebilir bir **kasa (crate) eklentisi**.

## Özellikler

- **3 animasyon tipi**
  - `CHEST` — klasik sandık: ortada hızlı ödül yanıp sönmesi, sonunda kazanılan ödülde durur.
  - `CSGO` — CS:GO tarzı yatay kayan şerit, yavaşlayarak kazanılan ödüle kilitlenir.
  - `ROULETTE` — Rulet temalı, farklı ses/görsel ile aynı mantık.
- **Fiziksel kasalar**: `/crate setlocation <kasa>` ile baktığın bloğu kasa yap, oyuncular
  doğru anahtarla sağ tıklayarak açsın.
- **Sanal açma**: `/crate open <kasa>` komutuyla, envanterindeki anahtar tüketilerek de açılabilir.
- **Anahtarlar** gerçek itemlerdir (PersistentDataContainer ile hangi kasaya ait oldukları işaretlenir),
  `/crate give <oyuncu> <kasa> <miktar>` ile dağıtılır.
- **Ağırlıklı şans sistemi**: her ödülün `chance` değeri toplam üzerinden orantılı seçilir.
- **Ödüller**: item verme, komut çalıştırma (ekonomi eklentisi vs. ile entegre edilebilir),
  `display-only: true` ile sadece komutla ödül (item vermeden).
- **Nadir ödül duyurusu**: `broadcast-rare` + `rare-chance-threshold` ile efsanevi ödülleri sunucuya duyur.
- Tüm mesajlar `messages.yml` üzerinden özelleştirilebilir, hex renk (`&#RRGGBB`) desteklenir.
- Her kasa kendi `.yml` dosyasında tanımlanır — `plugins/CrateSystem/crates/` klasörüne
  istediğin kadar dosya ekleyebilirsin.

## Kurulum / Derleme (GitHub Actions ile)

1. Bu klasörü bir GitHub reposuna yükle (push et).
2. `.github/workflows/build.yml` otomatik olarak her push'ta Maven ile derler.
3. Actions sekmesinden **Artifacts** kısmındaki `CrateSystem` dosyasını indir,
   içindeki `CrateSystem.jar` dosyasını sunucunun `plugins/` klasörüne koy.

Yerelde derlemek istersen (JDK 21 + Maven gerekli):

```bash
mvn clean package
```

Çıktı: `target/CrateSystem.jar`

## Komutlar

| Komut | Açıklama | Yetki |
|---|---|---|
| `/crate list` | Kasaları listeler | - |
| `/crate open <kasa>` | Envanterdeki anahtarla kasa açar | - |
| `/crate give <oyuncu> <kasa> <miktar>` | Anahtar verir | `cratesystem.admin` |
| `/crate setlocation <kasa>` | Baktığın bloğu fiziksel kasa yapar | `cratesystem.admin` |
| `/crate removelocation` | Baktığın bloktaki kasayı kaldırır | `cratesystem.admin` |
| `/crate forceopen <oyuncu> <kasa>` | Anahtar harcamadan zorla açtırır | `cratesystem.admin` |
| `/crate reload` | Tüm configleri yeniden yükler | `cratesystem.admin` |

## Yeni kasa ekleme

`plugins/CrateSystem/crates/` klasörüne yeni bir `.yml` dosyası ekle (örneğin `vip.yml`).
İçeriği için `crates/example.yml` dosyasını örnek al. En önemli alanlar:

```yaml
id: vip
name: "&6&lVIP Kasa"
animation: CSGO          # CHEST | CSGO | ROULETTE
key-item: TRIPWIRE_HOOK
key-name: "&6VIP Anahtari"
broadcast-rare: true
rare-chance-threshold: 5.0

items:
  - material: DIAMOND
    amount: 3
    name: "&b3 Elmas"
    chance: 40.0
    commands: []

  - material: NETHERITE_INGOT
    amount: 1
    name: "&5Netherite"
    chance: 2.0
    glow: true
    commands:
      - "give %player% netherite_ingot 1"
```

`/crate reload` çalıştırdığında yeni kasa otomatik yüklenir.

## Proje yapısı

```
src/main/java/com/cratesystem/
  CratePlugin.java          -> ana sınıf
  crate/                    -> Crate, CrateReward, CrateManager, CrateAnimationType
  key/                      -> KeyManager (anahtar itemleri)
  location/                 -> LocationManager (fiziksel kasa konumları)
  animation/                -> ChestAnimation, CSGOAnimation, RouletteAnimation
  listener/                 -> sağ tık etkileşimi + envanter koruması
  command/                  -> /crate komutu
  util/                     -> ItemBuilder, ColorUtils
```

## Notlar

- Java 21 ve Paper API 1.21.4 kullanılarak yazılmıştır, sadece `provided` scope'ta
  paper-api'ye bağımlıdır — harici kütüphane / shading gerektirmez.
- Ekonomi entegrasyonu istersen ödüllerin `commands` listesine kendi ekonomi
  eklentinin komutunu (`eco give %player% <miktar>` gibi) ekleyebilirsin.

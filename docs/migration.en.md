# Migration Guide

## 0.x to 1.x

- Replace manual POI mapping with annotation model.
- Move custom conversion logic to `ValueConverter`.
- Move row validation to `ValueValidator`.
- Register engine implementations by SPI or Spring Beans.

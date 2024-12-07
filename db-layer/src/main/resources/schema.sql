CREATE TABLE IF NOT EXISTS mouse_movements (
    x Int16,
    y Int16,
    deltaX Int16,
    deltaY Int16,
    clientTimeStamp Float32,
    button Int8,
    target String
) ENGINE = MergeTree()
ORDER BY clientTimeStamp;

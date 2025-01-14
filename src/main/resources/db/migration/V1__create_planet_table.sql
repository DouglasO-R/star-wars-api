create table if not exists planet(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    climate VARCHAR(50),
    terrain VARCHAR(50),
)
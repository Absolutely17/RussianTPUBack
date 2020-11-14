package ru.tpu.russian.back.repository.media;

public interface IMediaRepository {

    /**
     * Загрузить изображение
     */
    String upload(byte[] image);
}

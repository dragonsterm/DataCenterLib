/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;

/**
 *
 * @author mahar
 */
public interface ICRUD<T> {
    boolean create(T item);
    T read(String id);
    boolean update(T item);
    boolean delete(String id);
    List<T> getAll();
}

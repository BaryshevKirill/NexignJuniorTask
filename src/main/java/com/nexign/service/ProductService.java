package com.nexign.service;

import com.nexign.dao.impl.ProductDaoImpl;
import com.nexign.models.Product;
import com.nexign.models.ProductHistories;
import com.nexign.models.dto.ProductDto;
import com.nexign.models.dto.ProductInfoDto;
import info.debatty.java.stringsimilarity.Levenshtein;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductDaoImpl productDaoImpl;

    public List findAll() {
        return ProductInfoDto.fromList(productDaoImpl.findAll());
    }

    public Object findById(int id) {
        return ProductInfoDto.createOneObject((Object[])productDaoImpl.findById(id));
    }

    public Object findByProductNameAndProducer(String productName, String producer) {
        return ProductInfoDto.createOneObject((Object[])productDaoImpl.findByProductNameAndProducer(productName,producer));
    }


    public List findProductsByName(String nameProduct) {

        Levenshtein l = new Levenshtein();

        List<Object[]> list = productDaoImpl.findAll();

        List<ProductDto> newList = new LinkedList<>();

        for(Object[] array : list) {

            for(Object obj : array){
                if(obj.getClass() != Product.class) {
                    continue;
                }

                if(l.distance(((Product)obj).getProductName(),nameProduct) <= nameProduct.length()/2 )  {
                    newList.add(ProductDto.fromEntity((Product) obj));
                }

            }
        }

        return newList;

    }


    public Object save(ProductInfoDto productInfoDto) {

        Product product = new Product(productInfoDto.getProductName(), productInfoDto.getProducer());
        ProductHistories productHistories = new ProductHistories(product.getId(), productInfoDto.getCalories(), productInfoDto.getCarbohydrate(), productInfoDto.getFat(), productInfoDto.getProteins());
        return productDaoImpl.save(product, productHistories);

    }


    public ProductHistories update(Integer id, ProductHistories productHistories) {

        return productDaoImpl.update(id,productHistories);
//        return productDaoImpl.update( new ProductHistories(id,productInfoDto.getCalories(),productInfoDto.getCarbohydrate(),productInfoDto.getFat(),productInfoDto.getProteins()));

    }

}

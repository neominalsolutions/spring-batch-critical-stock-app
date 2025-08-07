package com.mertalptekin.springbatchcriticalstockapp.batch.chunkoriented;

import com.mertalptekin.springbatchcriticalstockapp.dto.ProductChunkDto;
import com.mertalptekin.springbatchcriticalstockapp.dto.ProductChunkRequestDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.List;

@Component
public class ProductItemReader implements ItemReader<ProductChunkDto> {

    private Iterator<ProductChunkDto> iterator;
    private List<ProductChunkDto> productChunkDtoList;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ProductChunkDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        if(productChunkDtoList == null){
            fetchProducts(); // Ürünleri API'den çekiyoruz
            iterator = productChunkDtoList.iterator(); // Iterator oluşturuyoruz
        }

        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            productChunkDtoList = null; // Tüm veriler okunduğunda listeyi sıfırlıyoruz. eğer her bir request içinde okunan veri iterator üzerinden bittiyse yeniden veri çekmesi için ayarla.
            return null; // Tüm veriler okundu
        }
    }

    private void fetchProducts() {
        String apiUrl = "https://services.odata.org/northwind/northwind.svc/Products?$format=json"; // API endpoint

        // RestTemplate ile GET isteği gönderiyoruz
        var response = restTemplate.exchange(apiUrl, HttpMethod.GET,null, ProductChunkRequestDto.class);

        productChunkDtoList = response.getBody().getValue();
    }
}

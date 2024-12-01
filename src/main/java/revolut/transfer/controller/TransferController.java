package revolut.transfer.controller;

import jakarta.validation.Valid;
import revolut.transfer.dto.TransferDto;
import revolut.transfer.service.TransferService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;

import static io.micronaut.http.HttpStatus.CREATED;

@Controller("${transfer.uri}")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @Post
    public HttpResponse<Object> transfer(@Body @Valid TransferDto transferDto) {
        transferService.transfer(transferDto);
        return HttpResponse.status(CREATED);
    }
}

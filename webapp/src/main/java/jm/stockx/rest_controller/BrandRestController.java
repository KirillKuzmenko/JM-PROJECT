package jm.stockx.rest_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jm.stockx.BrandService;
import jm.stockx.FileStorageService;
import jm.stockx.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/brand")
public class BrandRestController {

    private final BrandService brandService;
    private final FileStorageService fileStorageService;

    @Autowired
    public BrandRestController(BrandService brandService, FileStorageService fileStorageService) {
        this.brandService = brandService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    @Operation(
            operationId = "updateBrandLogo",
            summary = "takes MultipartFile logoFileToUpdate and updates BrandLogo by Brand Id ",
            parameters = {
                    @Parameter(name = "brandId", description = "Brand Id", schema = @Schema(implementation = Long.class)),
                    @Parameter(name = "logoFileToUpdate", description = "MultipartFile Brand logo to update",
                            schema = @Schema(implementation = MultipartFile.class))},
            responses = {
                    @ApiResponse(responseCode = "400", description = "Unable to locate BrandDto with Id"),
                    @ApiResponse(responseCode = "200", description = "Brand logo was successfully updated"),
            }
    )
    public Response<?> updateBrandLogo(@RequestParam Long brandId, @RequestParam MultipartFile logoFileToUpdate) {
        if (!brandService.isBrandExist(brandId)) {
            return Response.error(HttpStatus.NOT_FOUND, "Unable to locate BrandDto with Id" + brandId);
        }
        String fileName = fileStorageService.uploadImage(logoFileToUpdate, "brands");
        if (!fileName.isEmpty()){
            brandService.updateBrandLogo(brandId,fileName);
        }
        return Response.ok(HttpStatus.NO_CONTENT, "Image was successfully updated");

    }
}

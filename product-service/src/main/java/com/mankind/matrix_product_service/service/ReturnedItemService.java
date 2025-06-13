package com.mankind.matrix_product_service.service;

import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemDTO;
import com.mankind.matrix_product_service.dto.returneditem.ReturnedItemResponseDTO;
import com.mankind.matrix_product_service.exception.ResourceNotFoundException;
import com.mankind.matrix_product_service.mapper.ReturnedItemMapper;
import com.mankind.matrix_product_service.model.ReturnedItem;
import com.mankind.matrix_product_service.repository.ProductRepository;
import com.mankind.matrix_product_service.repository.ReturnedItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReturnedItemService {
    private final ReturnedItemRepository returnedItemRepository;
    private final ProductRepository productRepository;
    private final ReturnedItemMapper returnedItemMapper;

    /**
     * Create a new returned item
     *
     * @param returnedItemDTO the returned item data
     * @return the created returned item
     */
    @Transactional
    public ReturnedItemResponseDTO createReturnedItem(ReturnedItemDTO returnedItemDTO) {
        // Validate that the product exists
        validateProductId(returnedItemDTO.getProductId());

        // Convert DTO to entity
        ReturnedItem returnedItem = returnedItemMapper.toEntity(returnedItemDTO);

        // Save the entity
        ReturnedItem savedReturnedItem = returnedItemRepository.save(returnedItem);

        // Convert entity to response DTO
        return returnedItemMapper.toResponseDTO(savedReturnedItem);
    }

    /**
     * Get all returned items
     *
     * @param pageable pagination information
     * @return a page of returned items
     */
    @Transactional(readOnly = true)
    public Page<ReturnedItemResponseDTO> getAllReturnedItems(Pageable pageable) {
        return returnedItemRepository.findAll(pageable)
                .map(returnedItemMapper::toResponseDTO);
    }

    /**
     * Get a returned item by ID
     *
     * @param id the ID of the returned item
     * @return the returned item
     */
    @Transactional(readOnly = true)
    public ReturnedItemResponseDTO getReturnedItemById(Long id) {
        ReturnedItem returnedItem = findReturnedItemById(id);
        return returnedItemMapper.toResponseDTO(returnedItem);
    }

    /**
     * Get returned items by user ID
     *
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return a page of returned items
     */
    @Transactional(readOnly = true)
    public Page<ReturnedItemResponseDTO> getReturnedItemsByUserId(Long userId, Pageable pageable) {
        return returnedItemRepository.findByUserId(userId, pageable)
                .map(returnedItemMapper::toResponseDTO);
    }

    /**
     * Get returned items by status
     *
     * @param status the status of the returned items
     * @param pageable pagination information
     * @return a page of returned items
     */
    @Transactional(readOnly = true)
    public Page<ReturnedItemResponseDTO> getReturnedItemsByStatus(String status, Pageable pageable) {
        return returnedItemRepository.findByStatus(status, pageable)
                .map(returnedItemMapper::toResponseDTO);
    }

    /**
     * Update a returned item
     *
     * @param id the ID of the returned item
     * @param returnedItemDTO the updated returned item data
     * @return the updated returned item
     */
    @Transactional
    public ReturnedItemResponseDTO updateReturnedItem(Long id, ReturnedItemDTO returnedItemDTO) {
        // Validate that the product exists
        validateProductId(returnedItemDTO.getProductId());

        // Find the returned item
        ReturnedItem returnedItem = findReturnedItemById(id);

        // Update the entity
        returnedItemMapper.updateEntity(returnedItem, returnedItemDTO);

        // Save the updated entity
        ReturnedItem updatedReturnedItem = returnedItemRepository.save(returnedItem);

        // Convert entity to response DTO
        return returnedItemMapper.toResponseDTO(updatedReturnedItem);
    }

    /**
     * Delete a returned item
     *
     * @param id the ID of the returned item
     */
    @Transactional
    public void deleteReturnedItem(Long id) {
        // Find the returned item
        ReturnedItem returnedItem = findReturnedItemById(id);

        // Delete the entity
        returnedItemRepository.delete(returnedItem);
    }

    /**
     * Find a returned item by ID
     *
     * @param id the ID of the returned item
     * @return the returned item
     * @throws ResourceNotFoundException if the returned item is not found
     */
    private ReturnedItem findReturnedItemById(Long id) {
        return returnedItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Returned item not found with id: " + id));
    }

    /**
     * Validate that a product exists
     *
     * @param productId the ID of the product
     * @throws ResourceNotFoundException if the product is not found
     */
    private void validateProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
    }
}
